package com.friszing.rates.module.currencycalculator.repository

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import kotlinx.coroutines.flow.Flow

interface CurrencyCalculatorRepository {

    @Throws(CurrencyCalculatorException::class)
    suspend fun fetchRates(): CurrencyRateList
}