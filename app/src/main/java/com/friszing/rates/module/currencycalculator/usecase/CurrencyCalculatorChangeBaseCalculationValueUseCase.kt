package com.friszing.rates.module.currencycalculator.usecase

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

interface CurrencyCalculatorChangeBaseCalculationValueUseCase {
    operator fun invoke(currencyCalculatorItem: CurrencyCalculatorItem)
}