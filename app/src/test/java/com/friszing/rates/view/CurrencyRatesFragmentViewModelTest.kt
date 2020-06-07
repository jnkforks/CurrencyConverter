package com.friszing.rates.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.repository.CurrencyRateRepository
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
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


@RunWith(MockitoJUnitRunner::class)
class CurrencyRatesFragmentViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var ratesRepository: CurrencyRateRepository

    @Mock
    private lateinit var currencyRatesItemListMapper: CurrencyRatesItemListMapper

    @Mock
    private lateinit var currencyRatesExceptionMapper: CurrencyRatesExceptionMapper

    private lateinit var viewModel: CurrencyRatesFragmentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `Should return the recent currency rates when it is fetched successfully`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyRateItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()

        // THEN
        viewModel.viewState.test()
            .assertValue(CurrencyRatesFragmentViewState(items = mockCurrencyRateItems))
    }

    @Test
    fun `Should show the error message when the fetch currency rates service fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(mock())
        whenever(currencyRatesExceptionMapper.map(any())).thenReturn(errorId)
        // WHEN
        viewModel = createViewModel()

        // THEN
        viewModel.viewState.test()
            .assertValue(CurrencyRatesFragmentViewState(error = errorId))
    }

    @Test
    fun `Should show the loading indicator before the fetch currency rates service is successful`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyRateItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList, 1000)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()
        val viewStateTest = viewModel.viewState.test()
        testDispatcher.advanceTimeBy(1000L)
        // THEN

        viewStateTest.assertValueHistory(
            CurrencyRatesFragmentViewState(loading = true),
            CurrencyRatesFragmentViewState(items = mockCurrencyRateItems)
        )
    }

    @Test
    fun `Should show the loading indicator before the fetch currency rates service fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(mock(), 1000L)
        whenever(currencyRatesExceptionMapper.map(any())).thenReturn(errorId)

        // WHEN
        viewModel = createViewModel()
        val viewStateTest = viewModel.viewState.test()
        testDispatcher.advanceTimeBy(1000L)

        // THEN
        viewStateTest.assertValueHistory(
            CurrencyRatesFragmentViewState(loading = true),
            CurrencyRatesFragmentViewState(error = errorId)
        )
    }

    private fun createViewModel(): CurrencyRatesFragmentViewModel {
        return CurrencyRatesFragmentViewModel(
            ratesRepository,
            currencyRatesItemListMapper,
            currencyRatesExceptionMapper
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

    private fun setUpCurrencyRatesItemListMapper(mockCurrencyRateItems: List<CurrencyRateItem>) {
        whenever(currencyRatesItemListMapper.map(any(), any())).thenReturn(mockCurrencyRateItems)
    }
}