package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.lang.Exception

class CurrencyCalculatorRepositoryImpl(
    private val service: CurrencyRateService,
    private val mapper: CurrencyRateListResponseMapper,
    private val repositoryConfiguration: CurrencyCalculatorRepositoryConfiguration,
    private val coroutineDispatcher: CoroutineDispatcher
) : CurrencyCalculatorRepository {

    override fun changeBaseCurrency(currency: String) {
        repositoryConfiguration.baseCurrency = currency
    }

    @Throws(CurrencyCalculatorException::class)
    override fun getRates(): Flow<CurrencyRateList> = flow {
        while (true) {
            var currencyRateList: CurrencyRateList
            try {
                currencyRateList =
                    mapper.map(service.fetchCurrencyRatesList(repositoryConfiguration.baseCurrency))
            } catch (exception: CurrencyCalculatorException) {
                throw exception
            } catch (exception: IOException) {
                throw CurrencyCalculatorConnectionErrorException(exception)
            } catch (exception: Exception) {
                throw CurrencyCalculatorGeneralException(exception)
            }

            emit(currencyRateList)
            delay(repositoryConfiguration.requestIntervalMillis)
        }

    }.buffer().flowOn(coroutineDispatcher)
}