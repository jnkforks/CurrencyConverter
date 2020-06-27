package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase
import com.friszing.rates.util.TestCoroutineRule
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCase

    @Mock
    private lateinit var changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase

    @Mock
    private lateinit var changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase

    private lateinit var viewModel: CurrencyCalculatorFragmentViewModel

    @Before
    fun setUp() {
        setUpFetchCurrenciesUseCase(mock())
    }

    @Test
    fun `Should show the recent currency rates when it is fetched successfully`() {
        // GIVEN
        val currencyCalculatorFragmentViewState = mock<CurrencyCalculatorFragmentViewState>()
        setUpFetchCurrenciesUseCase(currencyCalculatorFragmentViewState)

        // WHEN
        val viewModel = createViewModel()
        val testObserver = viewModel.viewState.test()

        // THEN
        testObserver
            .assertValue(
                currencyCalculatorFragmentViewState
            )
    }

    @Test
    fun `Should change the base currency when the user clicks the currency item in the list`() {
        // GIVEN
        val currencyCalculatorItem = mock<CurrencyCalculatorItem>()
        viewModel = createViewModel()

        // WHEN
        viewModel.onCurrencyItemClicked(currencyCalculatorItem)

        // THEN
        verify(changeBaseCalculationValueUseCase).invoke(currencyCalculatorItem)
    }

    @Test
    fun `Should change the base currency when the user changes the calculation value`() {
        // GIVEN
        val currencyCalculatorItem = mock<CurrencyCalculatorItem>()
        viewModel = createViewModel()

        // WHEN
        viewModel.onCurrencyCalculationValueChanged(currencyCalculatorItem)

        // THEN
        verify(changeCalculationValueUseCase).invoke(currencyCalculatorItem.value)
    }

    private fun createViewModel() = CurrencyCalculatorFragmentViewModel(
        fetchCurrenciesUseCase,
        changeCalculationValueUseCase,
        changeBaseCalculationValueUseCase
    )

    private fun setUpFetchCurrenciesUseCase(
        currencyCalculatorFragmentViewState: CurrencyCalculatorFragmentViewState
    ) {
        whenever(fetchCurrenciesUseCase.invoke()).thenReturn(
            flow {
                emit(currencyCalculatorFragmentViewState)
            }
        )
    }
}