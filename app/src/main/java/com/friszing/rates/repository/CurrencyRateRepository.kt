package com.friszing.rates.repository

import com.friszing.rates.exception.CurrencyRateListException
import com.friszing.rates.model.CurrencyRateList
import kotlinx.coroutines.flow.Flow

interface CurrencyRateRepository {
    fun changeBaseCurrency(currency: String)

    @Throws(CurrencyRateListException::class)
    fun getRates(): Flow<CurrencyRateList>
}