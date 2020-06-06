package com.friszing.rates.repository

import com.friszing.rates.configuration.CurrencyRateRepositoryConfiguration
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListGeneralException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.model.CurrencyRateListResponse
import com.friszing.rates.service.CurrencyRateListResponseMapper
import com.friszing.rates.service.CurrencyRateService
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyRateRepositoryImplTest {

    @Mock
    private lateinit var service: CurrencyRateService

    @Mock
    private lateinit var mapper: CurrencyRateListResponseMapper

    @Mock
    private lateinit var repositoryConfiguration: CurrencyRateRepositoryConfiguration

    lateinit var testDispatcher: TestCoroutineDispatcher

    private lateinit var repository: CurrencyRateRepositoryImpl

    @Before
    fun setUp() {
        setUpBaseCurrency("EUR")
        setUpRequestIntervalMillis(1000)
        setUpCurrencyRateListResponseMapper(mock())
        setUpCurrencyRateService(mock())

        testDispatcher = TestCoroutineDispatcher()
        repository = CurrencyRateRepositoryImpl(
            service,
            mapper,
            repositoryConfiguration,
            testDispatcher
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Should emit the currency rate list periodically with the provided interval`() =
        runBlockingTest {
            // GIVEN
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                val dataList = ratesFlow.take(4).toList()

                // THEN
                assertThat(dataList.size).isEqualTo(4)
            }

            testDispatcher.advanceTimeBy(5000)
        }

    @Test
    fun `Should fetch the currency rate list from the currency rate service with specified period`() =
        runBlockingTest {
            // GIVEN
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(4).toList()

                // THEN
                verify(service, times(4)).fetchCurrencyRatesList(any())
            }

            testDispatcher.advanceTimeBy(5000)
        }

    @Test
    fun `Should fetch the currency rate list from the currency rate service with the base currency`() =
        runBlockingTest {
            // GIVEN
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(1).toList()

                // THEN
                verify(service).fetchCurrencyRatesList("EUR")
            }

            testDispatcher.advanceTimeBy(2000)
        }

    @Test
    fun `Should map the currency rate list response with specified period`() =
        runBlockingTest {
            // GIVEN
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(4).toList()

                // THEN
                verify(mapper, times(4)).map(any())
            }

            testDispatcher.advanceTimeBy(5000)
        }

    @Test
    fun `Should change the base currency in the configuration when it is changed`() {
        // WHEN
        repository.changeBaseCurrency("USD")

        // THEN
        verify(repositoryConfiguration).baseCurrency = "USD"
    }

    @Test(expected = CurrencyRateListGeneralException::class)
    fun `Should throw the general exception when unhandled expected is thrown by the service`() =
        runBlockingTest {
            // GIVEN
            setUpCurrencyRateServiceWithException(Exception())
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(1).toList()
            }

            testDispatcher.advanceTimeBy(2000)
        }

    @Test(expected = CurrencyRateListGeneralException::class)
    fun `Should throw the general exception when unhandled expected is thrown by the mapper`() =
        runBlockingTest {
            // GIVEN
            setUpCurrencyRateListResponseMapperWithException(Exception())
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(1).toList()
            }

            testDispatcher.advanceTimeBy(2000)
        }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the parsing exception is thrown by the mapper`() =
        runBlockingTest {
            // GIVEN
            setUpCurrencyRateListResponseMapperWithException(
                CurrencyRateListParseException(null)
            )
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(1).toList()
            }

            testDispatcher.advanceTimeBy(2000)
        }


    private suspend fun setUpCurrencyRateServiceWithException(exception: Throwable) {
        whenever(service.fetchCurrencyRatesList(any())).doAnswer { throw exception }
    }

    private suspend fun setUpCurrencyRateListResponseMapperWithException(exception: Throwable) {
        whenever(mapper.map(any())).doAnswer { throw exception }
    }

    private fun setUpCurrencyRateService(currencyRateListResponse: CurrencyRateListResponse) {
        runBlocking {
            whenever(service.fetchCurrencyRatesList(any())).thenReturn(currencyRateListResponse)
        }
    }

    private fun setUpCurrencyRateListResponseMapper(currencyRateList: CurrencyRateList) {
        whenever(mapper.map(any())).thenReturn(currencyRateList)
    }

    private fun setUpRequestIntervalMillis(requestIntervalMillis: Long) {
        whenever(repositoryConfiguration.requestIntervalMillis).thenReturn(requestIntervalMillis)
    }

    private fun setUpBaseCurrency(currency: String) {
        whenever(repositoryConfiguration.baseCurrency).thenReturn(currency)
    }
}