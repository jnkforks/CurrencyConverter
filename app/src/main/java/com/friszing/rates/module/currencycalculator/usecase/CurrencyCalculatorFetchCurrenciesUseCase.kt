package com.friszing.rates.module.currencycalculator.usecase

import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentViewState
import kotlinx.coroutines.flow.Flow

interface CurrencyCalculatorFetchCurrenciesUseCase {
    operator fun invoke(): Flow<CurrencyCalculatorFragmentViewState>
}