package com.friszing.rates.view

import androidx.annotation.StringRes
import androidx.fragment.app.testing.launchFragmentInContainer
import com.friszing.rates.R
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListGeneralException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.repository.CurrencyRateRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyRatesFragmentTest {

    @Mock
    private lateinit var ratesRepository: CurrencyRateRepository

    @Mock
    private lateinit var currencyRatesItemListMapper: CurrencyRatesItemListMapper

    @Mock
    private lateinit var currencyRatesExceptionMapper: CurrencyRatesExceptionMapper

    @Test
    fun should_show_the_currency_rates_fragment() = currencyRatesPage {
        // GIVEN
        setUpCurrencyRateRepository(CurrencyRateList)
        setUpCurrencyRatesItemListMapper(CurrencyRateItems)

        // WHEN
        launchFragment()

        // THEN
        isVisible()
    }

    @Test
    fun should_show_the_current_rate_items_list() =
        currencyRatesPage {
            // GIVEN
            setUpCurrencyRateRepository(CurrencyRateList)
            setUpCurrencyRatesItemListMapper(CurrencyRateItems)

            // WHEN
            launchFragment()

            // THEN
            isCurrencyRatesListVisible()
        }

    @Test
    fun should_show_the_currencies_rates_in_the_currency_rate_items_list() = currencyRatesPage {
        // GIVEN
        setUpCurrencyRateRepository(CurrencyRateList, times = 4, delayMillis = 2000L)
        setUpCurrencyRatesItemListMapper(CurrencyRateItems)

        // WHEN
        launchFragment()

        // THEN
        checkCurrencyRateItems(CurrencyRateItems)
    }

    @Test
    fun should_show_the_loading_indicator_before_the_fetching_currency_rates_starts() =
        currencyRatesPage {
            // GIVEN
            setUpCurrencyRateRepository(CurrencyRateList, 2000)
            setUpCurrencyRatesItemListMapper(CurrencyRateItems)

            // WHEN
            launchFragment()

            // THEN
            isLoadingIndicatorVisible()
        }

    @Test
    fun should_show_the_snackbar_when_the_fetching_currency_rates_fails() {
        currencyRatesPage {
            // GIVEN
            setUpCurrencyRateRepositoryWithException(
                CurrencyRateListGeneralException(
                    null
                )
            )
            setUpCurrencyRatesExceptionMapper(R.string.rates_calculator__general_error_message)
            setUpCurrencyRatesItemListMapper(CurrencyRateItems)

            // WHEN
            launchFragment()

            // THEN
            isSnackbarVisible()
        }
    }

    private fun setUpCurrencyRatesExceptionMapper(@StringRes value: Int) {
        whenever(currencyRatesExceptionMapper.map(any())).thenReturn(value)
    }

    private fun setUpCurrencyRatesItemListMapper(currencyRateItems: List<CurrencyRateItem>) {
        whenever(currencyRatesItemListMapper.map(any(), any())).thenReturn(currencyRateItems)
    }

    private fun setUpCurrencyRateRepository(
        currencyRateList: CurrencyRateList,
        initialDelayMillis: Long = 0,
        delayMillis: Long = 0,
        times: Int = 1
    ) {
        whenever(ratesRepository.getRates()).thenReturn(flow {
            repeat(times) {
                delay(initialDelayMillis)
                emit(currencyRateList)
                delay(delayMillis)
            }
        })
    }

    private fun setUpCurrencyRateRepositoryWithException(
        throwable: Throwable
    ) {
        whenever(ratesRepository.getRates()).thenReturn(
            flow { throw throwable }
        )
    }

    private fun launchFragment() {
        val factory = CurrencyRatesFragmentFactory(
            CurrencyRatesFragmentViewModelFactory(
                ratesRepository,
                currencyRatesItemListMapper,
                currencyRatesExceptionMapper
            )
        )
        launchFragmentInContainer<CurrencyRatesFragment>(
            themeResId = R.style.Theme_MaterialComponents_NoActionBar,
            factory = factory
        )
    }

    private companion object {
        private const val BASE_CURRENCY = "EUR"
        private const val CURRENCY = "USD"
        private const val CURRENCY_RATE = 1.2
        private val CurrencyRateList = CurrencyRateList(
            BASE_CURRENCY,
            mapOf(CURRENCY to CURRENCY_RATE)
        )

        private val CurrencyRateItems = listOf(
            CurrencyRateItem(
                BASE_CURRENCY,
                100.0
            ),
            CurrencyRateItem(
                CURRENCY,
                120.0
            )
        )
    }
}