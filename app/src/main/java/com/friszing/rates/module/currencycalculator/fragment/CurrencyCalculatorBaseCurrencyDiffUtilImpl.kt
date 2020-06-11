package com.friszing.rates.module.currencycalculator.fragment

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

class CurrencyCalculatorBaseCurrencyDiffUtilImpl : CurrencyCalculatorBaseCurrencyDiffUtil {

    override fun isBaseCurrenciesDifferent(
        oldList: List<CurrencyCalculatorItem>,
        newList: List<CurrencyCalculatorItem>
    ): Boolean {
        val oldCurrencySymbol = oldList.firstOrNull()?.currencyDetail?.currencySymbol
        val newCurrencySymbol = newList.firstOrNull()?.currencyDetail?.currencySymbol
        return oldCurrencySymbol != newCurrencySymbol
    }
}