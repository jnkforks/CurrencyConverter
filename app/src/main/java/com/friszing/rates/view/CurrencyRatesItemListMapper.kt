package com.friszing.rates.view

import com.friszing.rates.model.CurrencyRateList

interface CurrencyRatesItemListMapper {

    fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyRateItem>
}