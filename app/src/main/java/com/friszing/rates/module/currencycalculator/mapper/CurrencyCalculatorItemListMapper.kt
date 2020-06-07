package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

interface CurrencyCalculatorItemListMapper {

    fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem>
}