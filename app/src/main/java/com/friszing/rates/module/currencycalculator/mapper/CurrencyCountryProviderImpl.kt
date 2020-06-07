package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.Country
import java.util.Currency
import java.util.Locale

class CurrencyCountryProviderImpl : CurrencyCountryProvider {
    private val _currencyCountriesMap: Map<String, Country> = Locale.getAvailableLocales()
        .map { locale ->
            val currency = Currency.getInstance(locale.country)
            currency.currencyCode to Country(
                locale.displayName,
                locale.displayCountry
            )
        }.toMap()

    override val currencyCountriesMap: Map<String, Country>
        get() = _currencyCountriesMap
}