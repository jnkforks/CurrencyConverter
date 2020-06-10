package com.friszing.rates.currencycalculator

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
    override var baseCurrencyValue: Double
        get() = configurationPreferences.getFloat(
            KEY_CURRENCY_VALUE,
            CALCULATION_BASE_CURRENCY_DEFAULT_VALUE
        ).toDouble()
        set(value) {
            configurationPreferences.edit {
                putFloat(KEY_CURRENCY_VALUE, value.toFloat())
            }
        }

    override val requestIntervalMillis: Long
        get() = 1000L

    companion object {
        const val KEY_CURRENCY = "KEY_CURRENCY"
        const val KEY_CURRENCY_VALUE = "KEY_CURRENCY_VALUE"
        const val DEFAULT_CURRENCY = "EUR"
        private const val CALCULATION_BASE_CURRENCY_DEFAULT_VALUE = 100.0f
    }
}