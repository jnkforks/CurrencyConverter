package com.friszing.rates.configuration

interface CurrencyRateRepositoryConfiguration {
    var baseCurrency: String
    val requestIntervalMillis: Long
}