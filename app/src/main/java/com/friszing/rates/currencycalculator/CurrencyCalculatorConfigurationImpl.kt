package com.friszing.rates.currencycalculator

import android.content.SharedPreferences
import androidx.core.content.edit
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration

class CurrencyCalculatorConfigurationImpl(
    private val configurationPreferences: SharedPreferences
) : CurrencyCalculatorConfiguration {

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
    override var baseCalculationValue: Double
        get() = configurationPreferences.getFloat(
            KEY_CALCULATION,
            CALCULATION_BASE_CURRENCY_DEFAULT_VALUE
        ).toDouble()
        set(value) {
            configurationPreferences.edit {
                putFloat(KEY_CALCULATION, value.toFloat())
            }
        }

    override val requestIntervalMillis: Long
        get() = 1000L

    companion object {
        const val KEY_CURRENCY = "KEY_CURRENCY"
        const val KEY_CALCULATION = "KEY_CALCULATION"
        const val DEFAULT_CURRENCY = "EUR"
        private const val CALCULATION_BASE_CURRENCY_DEFAULT_VALUE = 100.0f
    }
}