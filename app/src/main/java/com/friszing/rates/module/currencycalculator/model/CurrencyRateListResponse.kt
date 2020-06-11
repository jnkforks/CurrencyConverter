package com.friszing.rates.module.currencycalculator.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyRateListResponse(
    val baseCurrency: String?,
    val rates: Map<String, Double>?
)