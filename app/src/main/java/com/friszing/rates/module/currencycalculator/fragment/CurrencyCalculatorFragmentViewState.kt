package com.friszing.rates.module.currencycalculator.fragment

import androidx.annotation.StringRes
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

data class CurrencyCalculatorFragmentViewState(
    val loading: Boolean = false,
    @StringRes val error: Int? = null,
    val items: List<CurrencyCalculatorItem> = emptyList()
)