package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyDetailProvider
import com.friszing.rates.module.currencycalculator.mapper.CurrencyValueCalculator
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList


class CurrencyCalculatorItemListMapperImpl(
    private val currencyDetailProvider: CurrencyDetailProvider,
    private val currencyValueCalculator: CurrencyValueCalculator
) : CurrencyCalculatorItemListMapper {

    override fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem> {
        val baseCurrencyItem = CurrencyCalculatorItem(
            currencyDetailProvider.provide(currencyRateList.baseCurrency),
            calculationValue
        )
        val ratesItems = mutableListOf(baseCurrencyItem)
        ratesItems += currencyRateList.rates.map { currencyRate ->
            CurrencyCalculatorItem(
                currencyDetailProvider.provide(currencyRate.key),
                currencyValueCalculator.calculate(currencyRate, calculationValue)
            )
        }
        return ratesItems
    }
}