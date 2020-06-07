package com.friszing.rates.view

import com.friszing.rates.model.CurrencyRateList

class CurrencyRatesItemListMapperImpl : CurrencyRatesItemListMapper {

    override fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyRateItem> {
        val ratesItems = mutableListOf(
            CurrencyRateItem(currencyRateList.baseCurrency, calculationValue)
        )
        ratesItems += currencyRateList.rates.map { exchangeRate ->
            CurrencyRateItem(exchangeRate.key, exchangeRate.value * calculationValue)
        }
        return ratesItems
    }
}