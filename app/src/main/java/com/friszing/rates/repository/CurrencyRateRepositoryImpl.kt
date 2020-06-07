package com.friszing.rates.repository

import com.friszing.rates.exception.CurrencyRateListException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.configuration.CurrencyRateRepositoryConfiguration
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListGeneralException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListConnectionErrorException
import com.friszing.rates.service.CurrencyRateListResponseMapper
import com.friszing.rates.service.CurrencyRateService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.lang.Exception

class CurrencyRateRepositoryImpl(
    private val service: CurrencyRateService,
    private val mapper: CurrencyRateListResponseMapper,
    private val repositoryConfiguration: CurrencyRateRepositoryConfiguration,
    private val coroutineDispatcher: CoroutineDispatcher
) : CurrencyRateRepository {

    override fun changeBaseCurrency(currency: String) {
        repositoryConfiguration.baseCurrency = currency
    }

    @Throws(CurrencyRateListException::class)
    override fun getRates(): Flow<CurrencyRateList> = flow {
        while (true) {
            var currencyRateList: CurrencyRateList
            try {
                currencyRateList =
                    mapper.map(service.fetchCurrencyRatesList(repositoryConfiguration.baseCurrency))
            } catch (exception: CurrencyRateListException) {
                throw exception
            } catch (exception: IOException) {
                throw CurrencyRateListConnectionErrorException(exception)
            } catch (exception: Exception) {
                throw CurrencyRateListGeneralException(exception)
            }

            emit(currencyRateList)
            delay(repositoryConfiguration.requestIntervalMillis)
        }

    }.buffer().flowOn(coroutineDispatcher)
}