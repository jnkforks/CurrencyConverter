package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRatesFragmentViewModel(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
) : ViewModel() {

    private var calculationValue =
        CALCULATION_BASE_CURRENCY_DEFAULT_VALUE
    private val _viewState = MutableLiveData<CurrencyCalculatorFragmentViewState>()

    val viewState: LiveData<CurrencyCalculatorFragmentViewState>
        get() = _viewState

    init {
        _viewState.value =
            CurrencyCalculatorFragmentViewState()

        viewModelScope.launch(Main) {
            ratesRepository.getRates()
                .onStart {
                    val currentState = requireNotNull(_viewState.value)
                    _viewState.value = currentState.copy(loading = true)
                }
                .catch {
                    val currentState = requireNotNull(_viewState.value)
                    _viewState.value = currentState.copy(
                        loading = false,
                        error = currencyCalculatorExceptionMapper.map(it)
                    )
                }
                .map { currencyCalculatorItemListMapper.map(it, calculationValue) }
                .onEach {
                    val currentState = requireNotNull(_viewState.value)
                    _viewState.value = currentState.copy(loading = false, items = it)
                }
                .collect()
        }
    }

    companion object {
        const val CALCULATION_BASE_CURRENCY_DEFAULT_VALUE = 100.0
    }
}