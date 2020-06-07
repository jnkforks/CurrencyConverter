package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository

class CurrencyRatesFragmentViewModelFactory(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
) : Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyRatesFragmentViewModel(
            ratesRepository,
            currencyCalculatorItemListMapper,
            currencyCalculatorExceptionMapper
        ) as T
}