package com.friszing.rates.module.currencycalculator.model

data class CurrencyRateListResponse(
    val baseCurrency: String?,
    val rates: Map<String, Double>?
)