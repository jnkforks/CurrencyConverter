package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.Country

interface CurrencyCountryProvider {
    val currencyCountriesMap: Map<String, Country>
}