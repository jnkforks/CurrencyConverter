package com.friszing.rates.currencycalculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListComposer
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.util.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.atMost
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorFetchCurrenciesUseCaseImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var ratesRepository: CurrencyCalculatorRepository

    @Mock
    private lateinit var configuration: CurrencyCalculatorConfiguration

    @Mock
    private lateinit var currencyCalculatorItemListComposer: CurrencyCalculatorItemListComposer

    @Mock
    private lateinit var currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper

    @InjectMocks
    private lateinit var fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCaseImpl

    @Test
    fun `Should show loading dialog before fetching the currency list`() = runBlocking {
        // GIVEN
        setUpCurrencyCalculatorRepository(mock())

        // WHEN
        val viewStateFlow = fetchCurrenciesUseCase.invoke()

        // THEN
        assertThat(viewStateFlow.first())
            .isEqualTo(
                CurrencyCalculatorFragmentViewState(loading = true)
            )
    }

    @Test
    fun `Should show error when there is an error in fetching the currency list`() =
        runBlocking {
            // GIVEN
            val errorId = -1
            setUpCurrencyCalculatorExceptionMapper(errorId)
            setUpCurrencyCalculatorRepositoryWithError(CurrencyCalculatorGeneralException())

            // WHEN
            val viewState = fetchCurrenciesUseCase.invoke().filter {
                it.error != null
            }.catch { }.first()

            // THEN
            assertThat(viewState).isEqualTo(CurrencyCalculatorFragmentViewState(error = errorId))
        }

    @Test
    fun `Should show the recently fetched currency list`() = runBlocking {
        // GIVEN
        val currencyCalculatorItems = mock<List<CurrencyCalculatorItem>>()
        whenever(currencyCalculatorItemListComposer.apply(any(), any())).thenReturn(
            currencyCalculatorItems
        )
        setUpCurrencyCalculatorRepository(mock())

        // WHEN
        val viewState = fetchCurrenciesUseCase.invoke().filter {
            it.items.isNotEmpty()
        }
            .catch { }
            .first()

        // THEN
        assertThat(viewState).isEqualTo(CurrencyCalculatorFragmentViewState(items = currencyCalculatorItems))
    }

    @Test
    fun `Should continue after network error exception`() = runBlocking<Unit> {
        // GIVEN
        val currencyRateList = CurrencyRateList("EUR", mapOf("USD" to 1.3))
        setUpCurrencyCalculatorRepositoryForRetryCase(currencyRateList)

        // WHEN
        fetchCurrenciesUseCase.invoke().filter { it.error == null && !it.loading }.catch { }
            .first()

        // THEN
        verify(currencyCalculatorItemListComposer, atLeastOnce()).apply(
            currencyRateList,
            configuration.baseCalculationValue
        )
    }

    @Test
    fun `Should not fetch the currency list when the thrown error is not network error`() =
        runBlocking<Unit> {
            // GIVEN
            setUpCurrencyCalculatorRepositoryWithError(CurrencyCalculatorGeneralException())

            // WHEN
            fetchCurrenciesUseCase.invoke().take(2).catch { }.toList()

            // THEN
            verify(
                ratesRepository,
                atMost(1)
            ).fetchRates()
        }

    private suspend fun setUpCurrencyCalculatorRepository(currencyRateList: CurrencyRateList) {
        whenever(ratesRepository.fetchRates()).thenReturn(currencyRateList)
    }

    private suspend fun setUpCurrencyCalculatorRepositoryForRetryCase(
        currencyRateList: CurrencyRateList
    ) {
        whenever(ratesRepository.fetchRates())
            .thenThrow(
                CurrencyCalculatorConnectionErrorException()
            )
            .thenReturn(currencyRateList)
    }

    private suspend fun setUpCurrencyCalculatorRepositoryWithError(throwable: Throwable) {
        whenever(ratesRepository.fetchRates()).thenThrow(throwable)
    }

    private fun setUpCurrencyCalculatorExceptionMapper(errorId: Int) {
        whenever(currencyCalculatorExceptionMapper.map(any())).thenReturn(errorId)
    }
}