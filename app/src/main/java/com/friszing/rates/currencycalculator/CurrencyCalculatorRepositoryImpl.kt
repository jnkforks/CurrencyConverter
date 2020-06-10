package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class CurrencyCalculatorRepositoryImpl(
    private val service: CurrencyRateService,
    private val responseMapper: CurrencyRateListResponseMapper,
    private val currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper,
    private val repositoryConfiguration: CurrencyCalculatorRepositoryConfiguration,
    private val coroutineDispatcher: CoroutineDispatcher
) : CurrencyCalculatorRepository {

    override fun changeBaseCurrency(currency: String) {
        repositoryConfiguration.baseCurrency = currency
    }

    @Throws(CurrencyCalculatorException::class)
    override fun getRates() = flow {
        while (true) {
            try {
                val currencyRateList = responseMapper.map(
                    service.fetchCurrencyRatesList(
                        repositoryConfiguration.baseCurrency
                    )
                )

                emit(
                    currencyCalculatorItemListMapper.map(
                        currencyRateList,
                        repositoryConfiguration.baseCurrencyValue
                    )
                )
                delay(repositoryConfiguration.requestIntervalMillis)
            } catch (exception: CurrencyCalculatorException) {
                throw exception
            } catch (exception: IOException) {
                throw CurrencyCalculatorConnectionErrorException(exception)
            } catch (exception: Exception) {
                throw CurrencyCalculatorGeneralException(exception)
            }
        }
    }.buffer().flowOn(coroutineDispatcher)
}