package com.friszing.rates.module.currencycalculator.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.util.formatCurrency
import com.friszing.rates.utils.atPosition


internal fun currencyCalculatorPage(action: CurrencyCalculatorPage.() -> Unit) =
    CurrencyCalculatorPage().action()

internal class CurrencyCalculatorPage {

    fun isVisible() = also {
        CURRENCY_RATES_FRAGMENT.check(matches(isDisplayed()))
    }

    fun isCurrencyRatesListVisible() = also {
        CURRENCY_RATES_LIST.check(matches(isDisplayed()))
    }

    fun isLoadingIndicatorVisible() = also {
        LOADING_INDICATOR.check(matches(isDisplayed()))
    }

    fun isSnackbarVisible() = also {
        SNACKBAR.check(matches(isDisplayed()))
    }

    fun checkCurrencyRateItems(currencyCalculatorItems: List<CurrencyCalculatorItem>) =
        currencyCalculatorItems.forEachIndexed { index, currencyRateItem ->
            checkListItem(index, currencyRateItem)
        }

    private fun checkListItem(index: Int, currencyCalculatorItem: CurrencyCalculatorItem) = also {
        CURRENCY_RATES_LIST
            .check(
                matches(
                    atPosition(
                        index,
                        hasDescendant(withText(currencyCalculatorItem.currencyDetail.currencySymbol))
                    )
                )
            )

        CURRENCY_RATES_LIST
            .check(
                matches(
                    atPosition(
                        index,
                        hasDescendant(withText(currencyCalculatorItem.value.formatCurrency()))
                    )
                )
            )
    }

    private companion object {

        private val CURRENCY_RATES_FRAGMENT =
            onView(withId(R.id.fragment_currency_rates))

        private val CURRENCY_RATES_LIST =
            onView(withId(R.id.ratesRecyclerView))

        private val LOADING_INDICATOR = onView(withId(R.id.loadingIndicator))

        private val SNACKBAR = onView(withId(com.google.android.material.R.id.snackbar_text))
    }
}