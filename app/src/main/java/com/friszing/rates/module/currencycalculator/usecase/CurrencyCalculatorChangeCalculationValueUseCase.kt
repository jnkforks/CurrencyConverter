package com.friszing.rates.module.currencycalculator.usecase

interface CurrencyCalculatorChangeCalculationValueUseCase {
    operator fun invoke(calculationValue: Double)
}