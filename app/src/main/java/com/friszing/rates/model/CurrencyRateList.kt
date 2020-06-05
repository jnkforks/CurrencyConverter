package com.friszing.rates.model

class CurrencyRateList(
    val baseCurrency: String,
    val rates: Map<String, Double>
)