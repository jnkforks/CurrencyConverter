package com.friszing.rates.module.currencycalculator.model

data class CurrencyCalculatorItem(
    val currencyCode: String,
    val value: Double,
    val country: Country? = null
)