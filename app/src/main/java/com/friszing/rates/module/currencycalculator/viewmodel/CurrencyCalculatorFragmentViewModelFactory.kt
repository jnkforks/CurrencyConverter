package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase

class CurrencyCalculatorFragmentViewModelFactory(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper,
    private val changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase,
    private val changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase
) : Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CurrencyCalculatorFragmentViewModel(
            ratesRepository,
            currencyCalculatorExceptionMapper,
            changeCalculationValueUseCase,
            changeBaseCalculationValueUseCase
        ) as T
}