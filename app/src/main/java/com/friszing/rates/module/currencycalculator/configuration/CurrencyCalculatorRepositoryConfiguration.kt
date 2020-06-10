package com.friszing.rates.module.currencycalculator.configuration

interface CurrencyCalculatorRepositoryConfiguration {
    var baseCurrency: String
    var baseCurrencyValue: Double
    val requestIntervalMillis: Long
}