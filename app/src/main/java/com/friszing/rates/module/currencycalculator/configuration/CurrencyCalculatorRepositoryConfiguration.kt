package com.friszing.rates.module.currencycalculator.configuration

interface CurrencyCalculatorRepositoryConfiguration {
    var baseCurrency: String
    var baseCalculationValue: Double
    val requestIntervalMillis: Long
}