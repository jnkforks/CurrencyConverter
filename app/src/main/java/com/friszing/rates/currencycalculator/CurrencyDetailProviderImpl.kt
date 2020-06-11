package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CurrencyDetailProvider
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import java.util.Currency

class CurrencyDetailProviderImpl :
    CurrencyDetailProvider {

    private val currencyCountryMap = mapOf(
        "EUR" to "https://flagpedia.net/data/org/w2560/eu.png",
        "AUD" to "https://flagpedia.net/data/flags/w2560/au.png",
        "BGN" to "https://flagpedia.net/data/flags/w2560/bg.png",
        "BRL" to "https://flagpedia.net/data/flags/w2560/br.png",
        "CAD" to "https://flagpedia.net/data/flags/w2560/ca.png",
        "CHF" to "https://flagpedia.net/data/flags/w2560/ch.png",
        "CNY" to "https://flagpedia.net/data/flags/w2560/cn.png",
        "CZK" to "https://flagpedia.net/data/flags/w2560/cz.png",
        "DKK" to "https://flagpedia.net/data/flags/w2560/dk.png",
        "GBP" to "https://flagpedia.net/data/flags/w2560/gb.png",
        "HKD" to "https://flagpedia.net/data/flags/w2560/hk.png",
        "HRK" to "https://flagpedia.net/data/flags/w2560/hr.png",
        "HUF" to "https://flagpedia.net/data/flags/w2560/hu.png",
        "IDR" to "https://flagpedia.net/data/flags/w2560/id.png",
        "ILS" to "https://flagpedia.net/data/flags/w2560/il.png",
        "INR" to "https://flagpedia.net/data/flags/w2560/in.png",
        "ISK" to "https://flagpedia.net/data/flags/w2560/is.png",
        "JPY" to "https://flagpedia.net/data/flags/w2560/jp.png",
        "KRW" to "https://flagpedia.net/data/flags/w2560/kr.png",
        "MXN" to "https://flagpedia.net/data/flags/w2560/mx.png",
        "MYR" to "https://flagpedia.net/data/flags/w2560/my.png",
        "NOK" to "https://flagpedia.net/data/flags/w2560/no.png",
        "NZD" to "https://flagpedia.net/data/flags/w2560/nz.png",
        "PHP" to "https://flagpedia.net/data/flags/w2560/ph.png",
        "PLN" to "https://flagpedia.net/data/flags/w2560/pl.png",
        "RON" to "https://flagpedia.net/data/flags/w2560/ro.png",
        "RUB" to "https://flagpedia.net/data/flags/w2560/ru.png",
        "SEK" to "https://flagpedia.net/data/flags/w2560/se.png",
        "SGD" to "https://flagpedia.net/data/flags/w2560/sg.png",
        "THB" to "https://flagpedia.net/data/flags/w2560/th.png",
        "USD" to "https://flagpedia.net/data/flags/w2560/us.png",
        "ZAR" to "https://flagpedia.net/data/flags/w2560/za.png"
    )

    override fun provide(currencyCode: String): CurrencyDetail {
        val currency = Currency.getInstance(currencyCode)
        return CurrencyDetail(
            currencyCode,
            currency.displayName,
            currencyCountryMap[currencyCode].orEmpty()
        )
    }
}