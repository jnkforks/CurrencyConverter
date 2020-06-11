package com.friszing.rates.module.currencycalculator.fragment

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

interface CurrencyCalculatorBaseCurrencyDiffUtil {

    fun isBaseCurrenciesDifferent(
        oldList: List<CurrencyCalculatorItem>,
        newList: List<CurrencyCalculatorItem>
    ): Boolean
}