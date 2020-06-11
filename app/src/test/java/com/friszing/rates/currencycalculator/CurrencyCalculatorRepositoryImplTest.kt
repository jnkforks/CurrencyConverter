package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorRepositoryImplTest {

    @Mock
    private lateinit var service: CurrencyRateService

    @Mock
    private lateinit var responseMapper: CurrencyRateListResponseMapper

    @Mock
    private lateinit var currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper

    @Mock
    private lateinit var repositoryConfiguration: CurrencyCalculatorRepositoryConfiguration

    lateinit var testDispatcher: TestCoroutineDispatcher

    private lateinit var repository: CurrencyCalculatorRepositoryImpl

    @Before
    fun setUp() {
        setUpBaseCurrency("EUR")
        setUpRequestIntervalMillis(1000)
        setUpCurrencyRateListResponseMapper(mock())
        setUpCurrencyRateService(mock())
        setUpCurrencyCalculatorItemListMapper(mock())
        testDispatcher = TestCoroutineDispatcher()
        repository =
            CurrencyCalculatorRepositoryImpl(
                service,
                responseMapper,
                currencyCalculatorItemListMapper,
                repositoryConfiguration,
                testDispatcher
            )
    }

    @Test
    fun `Should emit the currency rate list periodically with the provided interval`() =
        runBlockingTest {
            // GIVEN
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                val dataList = ratesFlow.catch { }.take(4).toList()

                // THEN
                assertThat(dataList.size).isEqualTo(4)
            }

            testDispatcher.advanceTimeBy(5000)
        }

    @Test
    fun `Should get the currency rate list with specified period`() =
        runBlockingTest {
            launch {
                // WHEN
                repository.getRates().catch { }.take(4).toList()

                // THEN
                verify(service, times(4)).fetchCurrencyRatesList(any())
            }

            testDispatcher.advanceTimeBy(5000)
        }

    @Test
    fun `Should get the currency rate list with the base currency`() =
        runBlockingTest {
            launch {
                // WHEN
                repository.getRates().catch { }.take(1).toList()

                // THEN
                verify(service).fetchCurrencyRatesList("EUR")
            }

            testDispatcher.advanceTimeBy(2000)
        }

    @Test
    fun `Should map the currency rate list response with specified period`() =
        runBlockingTest {
            // GIVEN
            launch {
                // WHEN
                repository.getRates().catch { }.take(4).toList()

                // THEN
                verify(responseMapper, times(4)).map(any())
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

    @Test(expected = CurrencyCalculatorGeneralException::class)
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

    @Test(expected = CurrencyCalculatorGeneralException::class)
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

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the parsing exception is thrown by the mapper`() =
        runBlockingTest {
            // GIVEN
            setUpCurrencyRateListResponseMapperWithException(
                CurrencyCalculatorParseException(null)
            )
            val ratesFlow = repository.getRates()
            launch {
                // WHEN
                ratesFlow.take(1).toList()
            }

            testDispatcher.advanceTimeBy(2000)
        }

    @Test(expected = CurrencyCalculatorConnectionErrorException::class)
    fun `Should throw the connection error exception when the network is not connected`() =
        runBlockingTest {
            // GIVEN
            setUpCurrencyRateServiceWithException(IOException())
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

    private fun setUpCurrencyCalculatorItemListMapper(
        currencyCalculatorItem: List<CurrencyCalculatorItem>
    ) {
        whenever(currencyCalculatorItemListMapper.map(any(), any())).thenReturn(
            currencyCalculatorItem
        )
    }

    private fun setUpRequestIntervalMillis(requestIntervalMillis: Long) {
        whenever(repositoryConfiguration.requestIntervalMillis).thenReturn(requestIntervalMillis)
    }

    private fun setUpBaseCurrency(currency: String) {
        whenever(repositoryConfiguration.baseCurrency).thenReturn(currency)
    }
}