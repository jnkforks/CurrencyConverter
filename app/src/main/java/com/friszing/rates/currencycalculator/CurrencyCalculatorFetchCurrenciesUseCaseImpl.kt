package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListComposer
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlin.system.measureTimeMillis

class CurrencyCalculatorFetchCurrenciesUseCaseImpl(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val configuration: CurrencyCalculatorConfiguration,
    private val currencyCalculatorItemListComposer: CurrencyCalculatorItemListComposer,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
) : CurrencyCalculatorFetchCurrenciesUseCase {

    private var lastCurrencyList = CurrencyRateList()
    private var exception: Throwable? = null

    override fun invoke() = producerFlow.combine(replicationFlow) { currencyList, _ -> currencyList }
        .onEach {
            exception = null
            lastCurrencyList = it
        }.retryWhen { cause, _ ->
            exception = cause
            emit(lastCurrencyList)
            cause is CurrencyCalculatorConnectionErrorException
        }
        .map {
            CurrencyCalculatorFragmentViewState(
                items = currencyCalculatorItemListComposer.apply(
                    it,
                    configuration.baseCalculationValue
                ),
                error = exception?.let(currencyCalculatorExceptionMapper::map)
            )
        }.onStart {
            emit(
                CurrencyCalculatorFragmentViewState(
                    loading = true
                )
            )
        }

    private val producerFlow = flow {
        while (true) {
            val elapsedTime = measureTimeMillis {
                emit(ratesRepository.fetchRates())
            }
            delay(configuration.requestIntervalMillis - elapsedTime)
        }
    }.flowOn(IO)

    private val replicationFlow = flow {
        while (true) emit(Unit)
    }.flowOn(Default)
}

