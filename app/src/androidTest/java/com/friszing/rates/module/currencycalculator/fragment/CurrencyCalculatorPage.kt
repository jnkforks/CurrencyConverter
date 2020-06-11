package com.friszing.rates.module.currencycalculator.fragment

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.R
import com.friszing.rates.utils.actionOnChild
import com.friszing.rates.utils.atPosition
import com.friszing.rates.utils.childOfViewAtPositionWithMatcher
import com.friszing.rates.utils.formatCurrency

internal fun currencyCalculatorPage(
    action: CurrencyCalculatorPage.() -> Unit
) = CurrencyCalculatorPage().action()

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

    fun selectCurrencyItem(position: Int) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder>(
                position
            )
        )
        CURRENCY_RATES_LIST.perform(
            actionOnItemAtPosition<CurrencyCalculatorItemViewHolder>(
                position,
                click()
            )
        )
    }

    fun changeCalculationValue(value: Double) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder>(
                0
            )
        )

        CURRENCY_RATES_LIST.perform(
            actionOnChild(
                replaceText(value.formatCurrency()),
                R.id.amount
            )
        )
    }

    fun changeCalculationValueWithEmptyText() = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder>(
                0
            )
        )

        CURRENCY_RATES_LIST.perform(
            actionOnChild(
                replaceText(""),
                R.id.amount
            )
        )
    }

    fun checkAllCurrencyRateItems(currencyCalculatorItems: List<CurrencyCalculatorItem>) =
        currencyCalculatorItems.forEachIndexed { index, currencyRateItem ->
            checkListItem(index, currencyRateItem)
        }

    fun checkCalculatedCurrencyRateItems(currencyCalculatorItems: List<CurrencyCalculatorItem>) =
        currencyCalculatorItems.forEachIndexed { index, currencyRateItem ->
            if (index > 0) {
                checkListItem(index, currencyRateItem)
            }
        }

    private fun checkListItem(index: Int, currencyCalculatorItem: CurrencyCalculatorItem) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder>(
                index
            )
        )
        CURRENCY_RATES_LIST.check(
            matches(
                atPosition(
                    index,
                    hasDescendant(
                        withText(
                            currencyCalculatorItem.currencyDetail.currencySymbol
                        )
                    )
                )
            )
        )
        CURRENCY_RATES_LIST.check(
            matches(
                atPosition(
                    index,
                    hasDescendant(
                        withText(
                            currencyCalculatorItem.value.formatCurrency()
                        )
                    )
                )
            )
        )
    }

    fun checkCurrencyInputEnabled(position: Int, enabled: Boolean) = also {
        CURRENCY_RATES_LIST.check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.amount,
                    position,
                    if (enabled) isEnabled() else isDisplayed()
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