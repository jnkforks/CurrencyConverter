package com.friszing.rates.module.currencycalculator.fragment

import androidx.annotation.StringRes
import androidx.fragment.app.testing.launchFragmentInContainer
import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorFragmentTest {

    @Mock
    private lateinit var ratesRepository: CurrencyCalculatorRepository

    @Mock
    private lateinit var currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper

    @Mock
    lateinit var currencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil

    @Test
    fun should_show_the_currency_rates_fragment() =
        currencyCalculatorPage {
            // GIVEN
            setUpCurrencyRateRepository(CurrencyRateItems)

            // WHEN
            launchFragment()

            // THEN
            isVisible()
        }

    @Test
    fun should_show_the_current_rate_items_list() =
        currencyCalculatorPage {
            // GIVEN
            setUpCurrencyRateRepository(CurrencyRateItems)

            // WHEN
            launchFragment()

            // THEN
            isCurrencyRatesListVisible()
        }

    @Test
    fun should_show_the_currencies_rates_in_the_currency_rate_items_list() =
        currencyCalculatorPage {
            // GIVEN
            setUpCurrencyRateRepository(
                CurrencyRateItems,
                times = 4,
                delayMillis = 2000L
            )

            // WHEN
            launchFragment()

            // THEN
            checkCurrencyRateItems(CurrencyRateItems)
        }

    @Test
    fun should_show_the_loading_indicator_before_the_fetching_currency_rates_starts() =
        currencyCalculatorPage {
            // GIVEN
            setUpCurrencyRateRepository(
                CurrencyRateItems,
                2000L
            )

            // WHEN
            launchFragment()

            // THEN
            isLoadingIndicatorVisible()
        }

    @Test
    fun should_show_the_snackbar_when_the_fetching_currency_rates_fails() =
        currencyCalculatorPage {
            // GIVEN
            setUpCurrencyRateRepositoryWithException(
                CurrencyCalculatorGeneralException(
                    null
                )
            )
            setUpCurrencyRatesExceptionMapper(R.string.rates_calculator__general_error_message)

            // WHEN
            launchFragment()

            // THEN
            isSnackbarVisible()
        }

    @Test
    fun should_change_fetched_currency_when_the_base_item_is_changed() = currencyCalculatorPage {
        // GIVEN
        setUpCurrencyRateRepository(CurrencyRateItems)
        launchFragment()

        // WHEN
        selectCurrencyItem(CURRENCY_USD_POSITION)

        // THEN
        verify(ratesRepository).changeBaseCurrency(CURRENCY_USD)
    }

    private fun setUpCurrencyRatesExceptionMapper(
        @StringRes value: Int
    ) {
        whenever(currencyCalculatorExceptionMapper.map(any()))
            .thenReturn(value)
    }

    private fun setUpCurrencyRateRepository(
        currencyRateList: List<CurrencyCalculatorItem>,
        initialDelayMillis: Long = 0,
        delayMillis: Long = 0,
        times: Int = 1
    ) {
        whenever(ratesRepository.getRates()).thenReturn(
            flow {
                repeat(times) {
                    delay(initialDelayMillis)
                    emit(currencyRateList)
                    delay(delayMillis)
                }
            }
        )
    }

    private fun setUpCurrencyRateRepositoryWithException(
        throwable: Throwable
    ) {
        whenever(ratesRepository.getRates()).thenReturn(
            flow { throw throwable }
        )
    }

    private fun launchFragment() {
        val factory =
            CurrencyCalculatorFragmentFactory(
                CurrencyCalculatorFragmentViewModelFactory(
                    ratesRepository,
                    currencyCalculatorExceptionMapper
                ),
                currencyDiffUtil
            )
        launchFragmentInContainer<CurrencyCalculatorFragment>(
            themeResId = R.style.AppTheme,
            factory = factory
        )
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