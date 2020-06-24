package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyCalculatorFragmentViewModel(
    private val ratesRepository: CurrencyCalculatorRepository,
    private val currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
) : ViewModel() {

    private val _viewState = MutableLiveData<CurrencyCalculatorFragmentViewState>()

    val viewState: LiveData<CurrencyCalculatorFragmentViewState>
        get() = _viewState

    init {
        _viewState.value =
            CurrencyCalculatorFragmentViewState()

        ratesRepository.getRates()
            .onStart {
                val currentState = requireNotNull(_viewState.value)
                _viewState.value = currentState.copy(loading = true)
            }.onEach {
                val currentState = requireNotNull(_viewState.value)
                _viewState.value = currentState.copy(loading = false, items = it, error = null)
            }.retryWhen { cause, _ ->
                if (shouldRetry(cause)) {
                    handleException(cause)
                    true
                } else {
                    false
                }
            }.catch { cause ->
                handleException(cause)
            }.launchIn(viewModelScope)
    }

    private fun handleException(cause: Throwable) {
        val currentState = requireNotNull(_viewState.value)
        _viewState.value = currentState.copy(
            loading = false,
            error = currencyCalculatorExceptionMapper.map(cause)
        )
    }

    fun onCurrencyItemClicked(currencyCalculatorItem: CurrencyCalculatorItem) =
        ratesRepository.changeBaseCurrency(currencyCalculatorItem)

    fun onCurrencyCalculationValueChanged(currencyCalculatorItem: CurrencyCalculatorItem) =
        ratesRepository.changeBaseCurrency(currencyCalculatorItem)

    private fun shouldRetry(cause: Throwable) =
        cause is CurrencyCalculatorConnectionErrorException
}