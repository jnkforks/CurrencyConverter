package com.friszing.rates.currencycalculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyRatesFragmentViewModel
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorFragmentViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var ratesRepository: CurrencyCalculatorRepository

    @Mock
    private lateinit var currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper

    @Mock
    private lateinit var currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper

    private lateinit var viewModel: CurrencyRatesFragmentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `Should return the recent currency rates when it is fetched successfully`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyCalculatorItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()

        // THEN
        viewModel.viewState.test()
            .assertValue(
                CurrencyCalculatorFragmentViewState(
                    items = mockCurrencyRateItems
                )
            )
    }

    @Test
    fun `Should show the error message when the fetch currency rates service fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(mock())
        whenever(currencyCalculatorExceptionMapper.map(any())).thenReturn(errorId)
        // WHEN
        viewModel = createViewModel()

        // THEN
        viewModel.viewState.test()
            .assertValue(
                CurrencyCalculatorFragmentViewState(
                    error = errorId
                )
            )
    }

    @Test
    fun `Should show the loading indicator before the fetch currency rates service is successful`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyCalculatorItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList, 1000)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()
        val viewStateTest = viewModel.viewState.test()
        testDispatcher.advanceTimeBy(1000L)
        // THEN

        viewStateTest.assertValueHistory(
            CurrencyCalculatorFragmentViewState(
                loading = true
            ),
            CurrencyCalculatorFragmentViewState(
                items = mockCurrencyRateItems
            )
        )
    }

    @Test
    fun `Should show the loading indicator before the fetch currency rates service fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(mock(), 1000L)
        whenever(currencyCalculatorExceptionMapper.map(any())).thenReturn(errorId)

        // WHEN
        viewModel = createViewModel()
        val viewStateTest = viewModel.viewState.test()
        testDispatcher.advanceTimeBy(1000L)

        // THEN
        viewStateTest.assertValueHistory(
            CurrencyCalculatorFragmentViewState(
                loading = true
            ),
            CurrencyCalculatorFragmentViewState(
                error = errorId
            )
        )
    }

    private fun createViewModel(): CurrencyRatesFragmentViewModel {
        return CurrencyRatesFragmentViewModel(
            ratesRepository,
            currencyCalculatorItemListMapper,
            currencyCalculatorExceptionMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun setUpCurrencyRateRepositoryWithError(
        exception: Throwable,
        delayMillis: Long = 0L
    ) {
        whenever(ratesRepository.getRates()).thenReturn(flow {
            delay(delayMillis)
            throw exception
        })
    }

    private fun setUpCurrencyRateRepository(
        mockCurrencyRateList: CurrencyRateList,
        delayMillis: Long = 0L
    ) {
        whenever(ratesRepository.getRates()).thenReturn(flow {
            delay(delayMillis)
            emit(mockCurrencyRateList)
        })
    }

    private fun setUpCurrencyRatesItemListMapper(mockCurrencyCalculatorItems: List<CurrencyCalculatorItem>) {
        whenever(currencyCalculatorItemListMapper.map(any(), any())).thenReturn(mockCurrencyCalculatorItems)
    }
}