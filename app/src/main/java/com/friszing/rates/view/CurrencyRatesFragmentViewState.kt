package com.friszing.rates.view

import androidx.annotation.StringRes

data class CurrencyRatesFragmentViewState(
    val loading: Boolean = false,
    @StringRes val error: Int? = null,
    val items: List<CurrencyRateItem> = emptyList()
)