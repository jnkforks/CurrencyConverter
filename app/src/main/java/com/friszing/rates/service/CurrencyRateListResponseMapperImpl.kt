package com.friszing.rates.service

import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.model.CurrencyRateListResponse

class CurrencyRateListResponseMapperImpl : CurrencyRateListResponseMapper {

    @Throws(CurrencyRateListParseException::class)
    override fun map(currencyRateListResponse: CurrencyRateListResponse?): CurrencyRateList {
        try {
            val response = checkNotNull(currencyRateListResponse)
            val baseCurrency = checkNotNull(response.baseCurrency)
            val rates = checkNotNull(response.rates)

            check(baseCurrency.isNotBlank())
            check(rates.isNotEmpty())

            return CurrencyRateList(
                baseCurrency,
                rates
            )
        } catch (exception: IllegalStateException) {
            throw CurrencyRateListParseException(exception)
        }
    }
}