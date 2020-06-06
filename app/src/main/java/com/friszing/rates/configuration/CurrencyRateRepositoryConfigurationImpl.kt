package com.friszing.rates.configuration

import android.content.SharedPreferences
import androidx.core.content.edit

class CurrencyRateRepositoryConfigurationImpl(
    private val configurationSharedPreferences: SharedPreferences
) : CurrencyRateRepositoryConfiguration {

    override var baseCurrency: String
        get() = requireNotNull(
            configurationSharedPreferences.getString(
                KEY_CURRENCY,
                DEFAULT_CURRENCY
            )
        )
        set(value) {
            configurationSharedPreferences.edit {
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