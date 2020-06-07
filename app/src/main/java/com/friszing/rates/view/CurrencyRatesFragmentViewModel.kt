package com.friszing.rates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friszing.rates.repository.CurrencyRateRepository
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CurrencyRatesFragmentViewModel(
    private val ratesRepository: CurrencyRateRepository,
    private val currencyRatesItemListMapper: CurrencyRatesItemListMapper,
    private val currencyRatesExceptionMapper: CurrencyRatesExceptionMapper
) : ViewModel() {

    private var calculationValue = CALCULATION_BASE_CURRENCY_DEFAULT_VALUE
    private val _viewState = MutableLiveData<CurrencyRatesFragmentViewState>()

    val viewState: LiveData<CurrencyRatesFragmentViewState>
        get() = _viewState

    init {
        _viewState.value = CurrencyRatesFragmentViewState()

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
                        error = currencyRatesExceptionMapper.map(it)
                    )
                }
                .map { currencyRatesItemListMapper.map(it, calculationValue) }
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