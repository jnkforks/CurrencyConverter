package com.friszing.rates.module.currencycalculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyCalculatorFragmentViewModel(
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
                }.map { currencyCalculatorItemListMapper.map(it, calculationValue) }
                .onEach {
                    val currentState = requireNotNull(_viewState.value)
                    _viewState.value = currentState.copy(loading = false, items = it, error = null)
                }.retryWhen { cause, _ ->
                    val currentState = requireNotNull(_viewState.value)
                    _viewState.value = currentState.copy(
                        loading = false,
                        error = currencyCalculatorExceptionMapper.map(cause)
                    )

                    if (shouldRetry(cause)) {
                        delay(RETRY_INTERVAL)
                        true
                    } else {
                        false
                    }
                }.collect()
        }
    }

    fun onCurrencyItemClicked(currencyCalculatorItem: CurrencyCalculatorItem) =
        ratesRepository.changeBaseCurrency(currencyCalculatorItem.currencyDetail.currencySymbol)

    private fun shouldRetry(cause: Throwable) =
        cause is CurrencyCalculatorConnectionErrorException

    companion object {
        private const val CALCULATION_BASE_CURRENCY_DEFAULT_VALUE = 100.0
        private const val RETRY_INTERVAL = 5000L
    }
}