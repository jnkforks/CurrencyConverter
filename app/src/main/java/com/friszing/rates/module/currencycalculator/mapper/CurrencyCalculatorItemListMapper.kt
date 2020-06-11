package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList

interface CurrencyCalculatorItemListMapper {

    fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem>
}