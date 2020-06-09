package com.friszing.rates.module.currencycalculator.fragment

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

class CurrencyCalculatorBaseCurrencyDiffUtilImpl : CurrencyCalculatorBaseCurrencyDiffUtil {

    override fun isBaseCurrenciesDifferent(
        oldList: List<CurrencyCalculatorItem>,
        newList: List<CurrencyCalculatorItem>
    ) = oldList.firstOrNull()?.currencyDetail?.currencySymbol !=
            newList.firstOrNull()?.currencyDetail?.currencySymbol
}