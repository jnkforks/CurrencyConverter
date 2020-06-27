package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyCalculatorFragmentViewModel(
    fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCase,
    private val changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase,
    private val changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase
) : ViewModel() {

    val viewState: LiveData<CurrencyCalculatorFragmentViewState> =
        fetchCurrenciesUseCase().asLiveData()

    fun onCurrencyItemClicked(currencyCalculatorItem: CurrencyCalculatorItem) =
        changeBaseCalculationValueUseCase(currencyCalculatorItem)

    fun onCurrencyCalculationValueChanged(
        currencyCalculatorItem: CurrencyCalculatorItem
    ) = changeCalculationValueUseCase(
        currencyCalculatorItem.value
    )
}