package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase

class CurrencyCalculatorFragmentViewModelFactory(
    private val fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCase,
    private val changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase,
    private val changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase
) : Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyCalculatorFragmentViewModel(
            fetchCurrenciesUseCase,
            changeCalculationValueUseCase,
            changeBaseCalculationValueUseCase
        ) as T
}