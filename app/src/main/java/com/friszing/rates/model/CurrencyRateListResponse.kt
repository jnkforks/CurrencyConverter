package com.friszing.rates.model

data class CurrencyRateListResponse(
    val baseCurrency: String?,
    val rates: Map<String, Double>?
)