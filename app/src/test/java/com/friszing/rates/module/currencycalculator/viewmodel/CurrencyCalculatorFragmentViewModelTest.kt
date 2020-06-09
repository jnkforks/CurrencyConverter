package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.util.TestCoroutineRule
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
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

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var ratesRepository: CurrencyCalculatorRepository

    @Mock
    private lateinit var currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper

    @Mock
    private lateinit var currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper

    private lateinit var viewModel: CurrencyCalculatorFragmentViewModel

    @Test
    fun `Should show the recent currency rates when it is fetched successfully`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyCalculatorItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(2000L)

        // THEN
        testObserver
            .assertValue(
                CurrencyCalculatorFragmentViewState(
                    items = mockCurrencyRateItems
                )
            )
    }

    @Test
    fun `Should show the error message when the currency rates fetching fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(mock())
        setUpCurrencyCalculatorExceptionMapper(errorId)

        // WHEN
        viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(2000L)

        // THEN
        testObserver.assertValue(
            CurrencyCalculatorFragmentViewState(
                error = errorId
            )
        )
    }

    @Test
    fun `Should show the last received items when the currency rates fetching fails`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyCalculatorExceptionMapper(errorId)
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyCalculatorItem>>()
        setUpCurrencyRateRepositoryWithErrorAfterSuccessfulReturn(mockCurrencyRateList)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)
        // WHEN
        viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(4000L)

        // THEN
        testObserver.assertValueHistory(
            CurrencyCalculatorFragmentViewState(
                loading = true
            ),
            CurrencyCalculatorFragmentViewState(
                items = mockCurrencyRateItems
            ),
            CurrencyCalculatorFragmentViewState(
                items = mockCurrencyRateItems,
                error = errorId
            )
        )
    }

    @Test
    fun `Should retry fetching the currency rates when the currency rates fetching fails because of the connection error exception`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryForRetryCase()
        setUpCurrencyCalculatorExceptionMapper(errorId)

        // WHEN
        viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(8000L)

        // THEN
        testObserver.assertHistorySize(4) // 2 loading 1 error 1 success
    }

    @Test
    fun `Should not retry fetching the currency rates when the thrown exception is not the connection error exception`() {
        // GIVEN
        val errorId = -1
        setUpCurrencyRateRepositoryWithError(NullPointerException())
        setUpCurrencyCalculatorExceptionMapper(errorId)

        // WHEN
        viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(8000L)

        // THEN
        testObserver.assertValueHistory(
            CurrencyCalculatorFragmentViewState(loading = true),
            CurrencyCalculatorFragmentViewState(error = errorId)
        )
    }

    @Test
    fun `Should show the loading indicator before the fetch currency rates service is successful`() {
        // GIVEN
        val mockCurrencyRateList = mock<CurrencyRateList>()
        val mockCurrencyRateItems = mock<List<CurrencyCalculatorItem>>()
        setUpCurrencyRateRepository(mockCurrencyRateList)
        setUpCurrencyRatesItemListMapper(mockCurrencyRateItems)

        // WHEN
        viewModel = createViewModel()
        val viewStateTest = viewModel.viewState.test()
        testCoroutineRule.dispatcher.advanceTimeBy(1000L)

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
    fun `Should show the loading indicator before the fetch currency rates service fails`() =
        runBlockingTest {
            // GIVEN
            val errorId = -1
            setUpCurrencyRateRepositoryWithError(mock(), 1000L)
            setUpCurrencyCalculatorExceptionMapper(errorId)

            // WHEN
            viewModel = createViewModel()
            val viewStateTest = viewModel.viewState.test()
            testCoroutineRule.dispatcher.advanceTimeBy(1000L)

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

    @Test
    fun `Should change the base currency when the user clicks the currency item in the list`() {
        // GIVEN
        val currencySymbol = "EUR"
        val currencyCalculatorItem = setUpCurrencyCalculatorItem(currencySymbol)
        viewModel = createViewModel()

        // WHEN
        viewModel.onCurrencyItemClicked(currencyCalculatorItem)

        // THEN
        verify(ratesRepository).changeBaseCurrency(currencySymbol)
    }

    private fun setUpCurrencyCalculatorItem(currencySymbol: String): CurrencyCalculatorItem {
        val currencyDetail = mock<CurrencyDetail> {
            on { it.currencySymbol } doReturn currencySymbol
        }
        return mock {
            on { it.currencyDetail } doReturn currencyDetail
        }
    }

    private fun createViewModel(): CurrencyCalculatorFragmentViewModel {
        return CurrencyCalculatorFragmentViewModel(
            ratesRepository,
            currencyCalculatorItemListMapper,
            currencyCalculatorExceptionMapper
        )
    }

    private fun setUpCurrencyCalculatorExceptionMapper(
        errorId: Int
    ) {
        whenever(currencyCalculatorExceptionMapper.map(any())).thenReturn(errorId)
    }

    private fun setUpCurrencyRateRepositoryForRetryCase(
        initialDelayMillis: Long = 1000L
    ) {
        var isNetworkErrorThrown = false

        val currencyRateListFlow = flow<CurrencyRateList> {
            delay(initialDelayMillis)

            if (!isNetworkErrorThrown) {
                isNetworkErrorThrown = true
                throw CurrencyCalculatorConnectionErrorException(null)
            } else {
                emit(mock())
            }
        }
        whenever(ratesRepository.getRates()).thenReturn(currencyRateListFlow)
    }

    private fun setUpCurrencyRateRepositoryWithError(
        exception: Throwable,
        delayMillis: Long = 1000L
    ) {
        whenever(ratesRepository.getRates()).thenReturn(
            flow {
                delay(delayMillis)
                throw exception
            }
        )
    }

    private fun setUpCurrencyRateRepositoryWithErrorAfterSuccessfulReturn(
        currencyRateList: CurrencyRateList
    ) {
        var isSuccessfulResponseEmitted = false

        whenever(ratesRepository.getRates()).thenReturn(
            flow {
                while (true) {
                    delay(1000)
                    if (!isSuccessfulResponseEmitted) {
                        isSuccessfulResponseEmitted = true
                        emit(currencyRateList)
                    } else {
                        throw Exception()
                    }
                }
            }
        )
    }

    private fun setUpCurrencyRateRepository(
        mockCurrencyRateList: CurrencyRateList,
        delayMillis: Long = 1000L
    ) {
        whenever(ratesRepository.getRates()).thenReturn(
            flow {
                delay(delayMillis)
                emit(mockCurrencyRateList)
            }
        )
    }

    private fun setUpCurrencyRatesItemListMapper(
        mockCurrencyCalculatorItems: List<CurrencyCalculatorItem>
    ) {
        whenever(currencyCalculatorItemListMapper.map(any(), any())).thenReturn(
            mockCurrencyCalculatorItems
        )
    }
}