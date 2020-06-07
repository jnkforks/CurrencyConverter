package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

class CurrencyCalculatorItemListMapperImpl :
    CurrencyCalculatorItemListMapper {

    override fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem> {
        val ratesItems = mutableListOf(
            CurrencyCalculatorItem(
                currencyRateList.baseCurrency,
                calculationValue
            )
        )
        ratesItems += currencyRateList.rates.map { exchangeRate ->
            CurrencyCalculatorItem(
                exchangeRate.key,
                exchangeRate.value * calculationValue
            )
        }
        return ratesItems
    }
}