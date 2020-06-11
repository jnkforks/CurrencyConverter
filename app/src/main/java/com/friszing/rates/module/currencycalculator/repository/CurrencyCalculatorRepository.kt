package com.friszing.rates.module.currencycalculator.repository

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import kotlinx.coroutines.flow.Flow

interface CurrencyCalculatorRepository {
    fun changeBaseCurrency(currencyCalculatorItem: CurrencyCalculatorItem)

    @Throws(CurrencyCalculatorException::class)
    fun getRates(): Flow<List<CurrencyCalculatorItem>>
}