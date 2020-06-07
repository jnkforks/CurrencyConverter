package com.friszing.rates.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.friszing.rates.R
import com.friszing.rates.util.formatCurrency
import com.friszing.rates.utils.atPosition


internal fun currencyRatesPage(action: CurrencyRatesPage.() -> Unit) = CurrencyRatesPage().action()


internal class CurrencyRatesPage {

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

    fun checkCurrencyRateItems(currencyRateItems: List<CurrencyRateItem>) =
        currencyRateItems.forEachIndexed { index, currencyRateItem ->
            checkListItem(index, currencyRateItem)
        }

    private fun checkListItem(index: Int, currencyRateItem: CurrencyRateItem) = also {
        CURRENCY_RATES_LIST
            .check(matches(atPosition(index, hasDescendant(withText(currencyRateItem.name)))))

        CURRENCY_RATES_LIST
            .check(
                matches(
                    atPosition(
                        index,
                        hasDescendant(withText(currencyRateItem.value.formatCurrency()))
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