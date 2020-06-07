package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.CurrencyDetail


interface CurrencyDetailProvider {
    fun provide(currencyCode: String): CurrencyDetail
}