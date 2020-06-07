package com.friszing.rates.module.currencycalculator.configuration

interface CurrencyCalculatorRepositoryConfiguration {
    var baseCurrency: String
    val requestIntervalMillis: Long
}