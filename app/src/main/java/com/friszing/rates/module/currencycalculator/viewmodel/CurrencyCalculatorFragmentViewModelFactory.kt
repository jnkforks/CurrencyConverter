package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase

class CurrencyCalculatorFragmentViewModelFactory(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper,
    private val changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase
) : Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyCalculatorFragmentViewModel(
            ratesRepository,
            currencyCalculatorExceptionMapper,
            changeCalculationValueUseCase
        ) as T
}