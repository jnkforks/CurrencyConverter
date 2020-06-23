package com.friszing.rates.module.currencycalculator.fragment

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.R
import com.friszing.rates.utils.actionOnChild
import com.friszing.rates.utils.atPosition
import com.friszing.rates.utils.childOfViewAtPositionWithMatcher
import com.friszing.rates.utils.formatCurrency
import com.google.common.truth.Truth.assertThat
import org.junit.Assert

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
        SNACKBAR.check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    fun selectCurrencyItem(position: Int) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder<TextView>>(
                position
            )
        )
        CURRENCY_RATES_LIST.perform(
            actionOnItemAtPosition<CurrencyCalculatorItemViewHolder<TextView>>(
                position,
                click()
            )
        )
    }

    fun scrollPageTo(position: Int) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder<TextView>>(
                position
            )
        )
    }

    fun changeCalculationValue(value: Double) = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder<TextView>>(
                0
            )
        )

        CURRENCY_RATES_LIST.perform(
            actionOnChild(
                replaceText(""),
                R.id.amount
            )
        )

        CURRENCY_RATES_LIST.perform(
            actionOnChild(
                typeText(value.formatCurrency()),
                R.id.amount
            )
        )
    }

    fun changeCalculationValueWithEmptyText() = also {
        CURRENCY_RATES_LIST.perform(
            scrollToPosition<CurrencyCalculatorItemViewHolder<TextView>>(
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
            scrollToPosition<CurrencyCalculatorItemViewHolder<TextView>>(
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

    fun checkKeyboardVisibility(isVisible: Boolean) = also {
        val inputMethodManager =
            InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        assertThat(inputMethodManager.isAcceptingText).isEqualTo(isVisible)
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