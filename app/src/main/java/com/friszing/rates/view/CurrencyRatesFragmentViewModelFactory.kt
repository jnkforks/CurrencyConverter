package com.friszing.rates.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.friszing.rates.repository.CurrencyRateRepository

class CurrencyRatesFragmentViewModelFactory(
    private val ratesRepository: CurrencyRateRepository,
    private val currencyRatesItemListMapper: CurrencyRatesItemListMapper,
    private val currencyRatesExceptionMapper: CurrencyRatesExceptionMapper
) : Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyRatesFragmentViewModel(
            ratesRepository,
            currencyRatesItemListMapper,
            currencyRatesExceptionMapper
        ) as T
}