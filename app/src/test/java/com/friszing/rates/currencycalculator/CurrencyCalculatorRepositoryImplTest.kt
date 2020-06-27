package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorRepositoryImplTest {

    @Mock
    private lateinit var service: CurrencyRateService

    @Mock
    private lateinit var responseMapper: CurrencyRateListResponseMapper

    @Mock
    private lateinit var configuration: CurrencyCalculatorConfiguration

    private lateinit var repository: CurrencyCalculatorRepositoryImpl

    @Before
    fun setUp() {
        setUpBaseCurrency("EUR")
        setUpCurrencyRateListResponseMapper(mock())
        setUpCurrencyRateService(mock())
        repository =
            CurrencyCalculatorRepositoryImpl(
                service,
                responseMapper,
                configuration
            )
    }

    @Test
    fun `Should fetch currency rate list with the selected base currency`() =
        runBlocking<Unit> {
            // WHEN
            repository.fetchRates()

            // THEN
            verify(service).fetchCurrencyRatesList("EUR")
        }

    @Test
    fun `Should map currency rate list when the curreny list is fetched from the service`() =
        runBlocking<Unit> {
            // GIVEN
            val currencyRateListResponse = mock<CurrencyRateListResponse>()
            setUpCurrencyRateService(currencyRateListResponse)
            // WHEN
            repository.fetchRates()

            // THEN
            verify(responseMapper).map(currencyRateListResponse)
        }

    @Test(expected = CurrencyCalculatorGeneralException::class)
    fun `Should throw the general exception when unhandled expected is thrown by the service`() =
        runBlocking<Unit> {
            // GIVEN
            setUpCurrencyRateServiceWithException(Exception())

            // WHEN
            repository.fetchRates()
        }

    @Test(expected = CurrencyCalculatorGeneralException::class)
    fun `Should throw the general exception when unhandled expected is thrown by the mapper`() =
        runBlocking<Unit> {
            // GIVEN
            setUpCurrencyRateListResponseMapperWithException(Exception())

            // WHEN
            repository.fetchRates()
        }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the parsing exception is thrown by the mapper`() =
        runBlocking<Unit> {
            // GIVEN
            setUpCurrencyRateListResponseMapperWithException(
                CurrencyCalculatorParseException(null)
            )

            // WHEN
            repository.fetchRates()
        }

    @Test(expected = CurrencyCalculatorConnectionErrorException::class)
    fun `Should throw the connection error exception when the network is not connected`() =
        runBlocking<Unit> {
            // GIVEN
            setUpCurrencyRateServiceWithException(IOException())

            // WHEN
            repository.fetchRates()
        }

    private suspend fun setUpCurrencyRateServiceWithException(exception: Throwable) {
        whenever(service.fetchCurrencyRatesList(any())).doAnswer { throw exception }
    }

    private fun setUpCurrencyRateListResponseMapperWithException(exception: Throwable) {
        whenever(responseMapper.map(any())).doAnswer { throw exception }
    }

    private fun setUpCurrencyRateService(currencyRateListResponse: CurrencyRateListResponse) {
        runBlocking {
            whenever(service.fetchCurrencyRatesList(any())).thenReturn(currencyRateListResponse)
        }
    }

    private fun setUpCurrencyRateListResponseMapper(currencyRateList: CurrencyRateList) {
        whenever(responseMapper.map(any())).thenReturn(currencyRateList)
    }

    private fun setUpBaseCurrency(currency: String) {
        whenever(configuration.baseCurrency).thenReturn(currency)
    }
}