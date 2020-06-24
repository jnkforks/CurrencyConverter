package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase

class CurrencyCalculatorChangeBaseCalculationValueUseCaseImpl(
    private val configuration: CurrencyCalculatorConfiguration
) : CurrencyCalculatorChangeBaseCalculationValueUseCase {

    override fun invoke(currencyCalculatorItem: CurrencyCalculatorItem) {
        configuration.baseCurrency = currencyCalculatorItem.currencyDetail.currencySymbol
        configuration.baseCalculationValue = currencyCalculatorItem.value
    }
}