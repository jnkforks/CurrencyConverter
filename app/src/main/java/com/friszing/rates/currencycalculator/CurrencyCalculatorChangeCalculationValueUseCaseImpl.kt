package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase

class CurrencyCalculatorChangeCalculationValueUseCaseImpl(
    private val configuration: CurrencyCalculatorConfiguration
) : CurrencyCalculatorChangeCalculationValueUseCase {

    override operator fun invoke(calculationValue: Double) {
        configuration.baseCalculationValue = calculationValue
    }
}