package com.friszing.rates.module.currencycalculator.mapper

class CountryCodeMapperImpl(private val currencyCountryProvider: CurrencyCountryProvider) :
    CountryCodeMapper {

    override fun map(currencyCode: String) =
        currencyCountryProvider.currencyCountriesMap[currencyCode]
}