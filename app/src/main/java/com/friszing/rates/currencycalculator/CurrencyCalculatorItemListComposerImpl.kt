package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListComposer
import com.friszing.rates.module.currencycalculator.mapper.CurrencyDetailProvider
import com.friszing.rates.module.currencycalculator.mapper.CurrencyValueCalculator
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList


class CurrencyCalculatorItemListComposerImpl(
    private val currencyDetailProvider: CurrencyDetailProvider,
    private val currencyValueCalculator: CurrencyValueCalculator
) : CurrencyCalculatorItemListComposer {

    override fun apply(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem> {
        if (currencyRateList.baseCurrency.isBlank()) return emptyList()

        val baseCurrencyItem = CurrencyCalculatorItem(
            currencyDetailProvider.provide(currencyRateList.baseCurrency),
            calculationValue
        )
        val ratesItems = mutableSetOf(baseCurrencyItem)
        ratesItems += currencyRateList.rates.map { currencyRate ->
            CurrencyCalculatorItem(
                currencyDetailProvider.provide(currencyRate.key),
                currencyValueCalculator.calculate(currencyRate, calculationValue)
            )
        }
        return ratesItems.toList()
    }
}