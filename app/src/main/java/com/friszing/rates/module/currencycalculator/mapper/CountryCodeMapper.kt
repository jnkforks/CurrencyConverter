package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.Country

interface CountryCodeMapper {
    fun map(currencyCode: String): Country?
}