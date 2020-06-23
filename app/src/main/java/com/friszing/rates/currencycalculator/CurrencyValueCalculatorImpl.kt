package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CurrencyRate
import com.friszing.rates.module.currencycalculator.mapper.CurrencyValueCalculator

class CurrencyValueCalculatorImpl : CurrencyValueCalculator {
    override fun calculate(
        currencyRate: CurrencyRate,
        calculationValue: Double
    ) = currencyRate.value * calculationValue
}