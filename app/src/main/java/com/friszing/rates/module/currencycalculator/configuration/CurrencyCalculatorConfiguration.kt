package com.friszing.rates.module.currencycalculator.configuration

interface CurrencyCalculatorConfiguration {
    var baseCurrency: String
    var baseCalculationValue: Double
    val requestIntervalMillis: Long
}