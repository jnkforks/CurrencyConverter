package com.friszing.rates.module.currencycalculator.fragment

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class CurrencyCalculatorBaseCurrencyDiffUtilImplTest {

    @Test
    fun `Should return true when the base items are different`() {
        // GIVEN
        val currencyCalculatorBaseCurrencyDiffUtil = CurrencyCalculatorBaseCurrencyDiffUtilImpl()

        // WHEN
        val baseCurrencyChanged = currencyCalculatorBaseCurrencyDiffUtil.isBaseCurrenciesDifferent(
            listOf(CurrencyCalculatorItem(CurrencyDetail("TRY", "", ""), 0.0)),
            listOf(CurrencyCalculatorItem(CurrencyDetail("EUR", "", ""), 0.0))
        )

        // THEN
        assertThat(baseCurrencyChanged).isTrue()
    }

    @Test
    fun `Should return false when the base items are same`() {
        // GIVEN
        val currencyCalculatorBaseCurrencyDiffUtil = CurrencyCalculatorBaseCurrencyDiffUtilImpl()

        // WHEN
        val baseCurrencyChanged = currencyCalculatorBaseCurrencyDiffUtil.isBaseCurrenciesDifferent(
            listOf(CurrencyCalculatorItem(CurrencyDetail("TRY", "", ""), 0.0)),
            listOf(CurrencyCalculatorItem(CurrencyDetail("TRY", "", ""), 0.0))
        )

        // THEN
        assertThat(baseCurrencyChanged).isFalse()
    }
}