package com.friszing.rates.module.currencycalculator.model

data class CurrencyRateList(
    val baseCurrency: String = "",
    val rates: Map<String, Double> = emptyMap()
)