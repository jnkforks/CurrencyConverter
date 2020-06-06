package com.friszing.rates.service

import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.model.CurrencyRateListResponse

interface CurrencyRateListResponseMapper {
    @Throws(CurrencyRateListParseException::class)
    fun map(currencyRateListResponse: CurrencyRateListResponse?): CurrencyRateList
}