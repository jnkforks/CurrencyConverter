package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import java.io.IOException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

class CurrencyCalculatorRepositoryImpl(
    private val service: CurrencyRateService,
    private val responseMapper: CurrencyRateListResponseMapper,
    private val configuration: CurrencyCalculatorConfiguration
) : CurrencyCalculatorRepository {

    @Throws(CurrencyCalculatorException::class)
    override suspend fun fetchRates() = withContext(IO) {
        try {
            responseMapper.map(
                service.fetchCurrencyRatesList(
                    configuration.baseCurrency
                )
            )
        } catch (exception: Exception) {
            handleException(exception)
        }
    }

    private fun handleException(throwable: Throwable): Nothing {
        throw when (throwable) {
            is CurrencyCalculatorException -> throwable
            is IOException, is UnknownHostException -> CurrencyCalculatorConnectionErrorException(
                throwable
            )
            else -> CurrencyCalculatorGeneralException(throwable)
        }
    }
}