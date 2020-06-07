package com.friszing.rates.configuration

import android.content.SharedPreferences
import androidx.core.content.edit
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration

class CurrencyCalculatorRepositoryConfigurationImpl(
    private val configurationPreferences: SharedPreferences
) : CurrencyCalculatorRepositoryConfiguration {

    override var baseCurrency: String
        get() = requireNotNull(
            configurationPreferences.getString(
                KEY_CURRENCY,
                DEFAULT_CURRENCY
            )
        )
        set(value) {
            configurationPreferences.edit {
                putString(KEY_CURRENCY, value)
            }
        }

    override val requestIntervalMillis: Long
        get() = 1000L

    companion object {
        const val KEY_CURRENCY = "KEY_CURRENCY"
        const val DEFAULT_CURRENCY = "EUR"
    }
}