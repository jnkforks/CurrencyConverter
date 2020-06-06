package com.friszing.rates.model

data class CurrencyRateList(
    val baseCurrency: String,
    val rates: Map<String, Double>
)