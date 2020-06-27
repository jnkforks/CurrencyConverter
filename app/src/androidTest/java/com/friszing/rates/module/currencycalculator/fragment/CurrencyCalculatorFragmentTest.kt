package com.friszing.rates.module.currencycalculator.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle.State.RESUMED
import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorFragmentTest {

    @Mock
    private lateinit var fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCase

    @Mock
    private lateinit var changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase

    @Mock
    private lateinit var changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase

    @Mock
    lateinit var currencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil

    @Before
    fun setUp() {
        setUpChangeCalculationValueUseCase(CurrencyRateItems)
    }

    @Test
    fun should_show_the_currency_rates_fragment() =
        currencyCalculatorPage {
            // WHEN
            launchFragment()

            // THEN
            isVisible()
        }

    @Test
    fun should_show_the_current_rate_items_list() =
        currencyCalculatorPage {
            // WHEN
            launchFragment()

            // THEN
            isCurrencyRatesListVisible()
        }

    @Test
    fun should_show_the_currencies_rates_in_the_currency_rate_items_list() =
        currencyCalculatorPage {
            // WHEN
            launchFragment()

            // THEN
            checkAllCurrencyRateItems(CurrencyRateItems)
        }

    @Test
    fun should_show_the_loading_indicator_before_the_fetching_currency_rates_starts() =
        currencyCalculatorPage {
            // GIVEN
            setUpChangeCalculationValueUseCaseForLoading()

            // WHEN
            launchFragment()

            // THEN
            isLoadingIndicatorVisible()
        }

    @Test
    fun should_show_the_snackbar_when_the_fetching_currency_rates_fails() =
        currencyCalculatorPage {
            // GIVEN
            setUpChangeCalculationValueUseCaseForError()

            // WHEN
            launchFragment()

            // THEN
            isSnackbarVisible()
        }

    @Test
    fun should_change_fetched_currency_when_the_base_item_is_changed() =
        currencyCalculatorPage {
            // GIVEN
            setUpChangeCalculationValueUseCase(CurrencyRateItems)
            launchFragment()

            // WHEN
            selectCurrencyItem(CURRENCY_USD_POSITION)

            // THEN
            verify(changeBaseCalculationValueUseCase).invoke(CurrencyRateItems[CURRENCY_USD_POSITION])
        }

    @Test
    fun should_show_the_keyboard_when_the_base_currency_input_is_clicked() =
        currencyCalculatorPage {
            // GIVEN
            setUpChangeCalculationValueUseCase(CurrencyRateItems)
            launchFragment()

            // WHEN
            selectCurrencyItem(CURRENCY_USD_POSITION)

            // THEN
            checkKeyBoardIsShown(0, true)
        }

    @Test
    fun should_not_show_the_keyboard_when_the_other_currencies_input_is_clicked() =
        currencyCalculatorPage {
            // GIVEN
            setUpChangeCalculationValueUseCase(CurrencyRateItems)
            launchFragment()

            // WHEN
            selectCurrencyItem(CURRENCY_USD_POSITION)

            // THEN
            checkKeyBoardIsShown(1, false)
        }

    private fun setUpChangeCalculationValueUseCase(
        currencyRateList: List<CurrencyCalculatorItem>
    ) {
        whenever(fetchCurrenciesUseCase.invoke()).thenReturn(
            flow {
                emit(CurrencyCalculatorFragmentViewState(items = currencyRateList))
                delay(1000)
            }
        )
    }

    private fun setUpChangeCalculationValueUseCaseForError() {
        whenever(fetchCurrenciesUseCase.invoke()).thenReturn(
            flow {
                emit(CurrencyCalculatorFragmentViewState(error = R.string.rates_calculator__network_error_message))
                delay(1000)
            }
        )
    }

    private fun setUpChangeCalculationValueUseCaseForLoading(
    ) {
        whenever(fetchCurrenciesUseCase.invoke()).thenReturn(
            flow {
                emit(CurrencyCalculatorFragmentViewState(loading = true))
                delay(1000)
            }
        )
    }

    private fun launchFragment() {
        val factory =
            CurrencyCalculatorFragmentFactory(
                CurrencyCalculatorFragmentViewModelFactory(
                    fetchCurrenciesUseCase,
                    changeCalculationValueUseCase,
                    changeBaseCalculationValueUseCase
                ),
                currencyDiffUtil
            )
        val fragmentScenario = launchFragmentInContainer<CurrencyCalculatorFragment>(
            themeResId = R.style.AppTheme,
            factory = factory
        )
        fragmentScenario.moveToState(RESUMED)
    }

    private companion object {
        private const val BASE_CURRENCY = "EUR"
        private const val CURRENCY_USD = "USD"
        private const val CURRENCY_USD_POSITION = 1

        private val CurrencyRateItems = listOf(
            CurrencyCalculatorItem(
                CurrencyDetail(
                    BASE_CURRENCY,
                    "Euro",
                    "https://flagpedia.net/data/org/w2560/eu.png"
                ),
                100.0
            ),
            CurrencyCalculatorItem(
                CurrencyDetail(
                    CURRENCY_USD,
                    "US Dollars",
                    "https://flagpedia.net/data/flags/w2560/us.png"
                ),
                120.0
            )
        )
    }
}