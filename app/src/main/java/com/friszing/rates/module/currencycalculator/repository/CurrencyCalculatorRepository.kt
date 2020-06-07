package com.friszing.rates.module.currencycalculator.repository

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import kotlinx.coroutines.flow.Flow

interface CurrencyCalculatorRepository {
    fun changeBaseCurrency(currency: String)

    @Throws(CurrencyCalculatorException::class)
    fun getRates(): Flow<CurrencyRateList>
}