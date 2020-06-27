package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse

class CurrencyRateListResponseMapperImpl :
    CurrencyRateListResponseMapper {

    @Throws(CurrencyCalculatorParseException::class)
    override fun map(currencyRateListResponse: CurrencyRateListResponse?): CurrencyRateList {
        try {
            val response = checkNotNull(currencyRateListResponse)
            val baseCurrency = checkNotNull(response.base)
            val rates = checkNotNull(response.rates)

            check(baseCurrency.isNotBlank())
            check(rates.isNotEmpty())

            return CurrencyRateList(
                baseCurrency,
                rates
            )
        } catch (exception: IllegalStateException) {
            throw CurrencyCalculatorParseException(exception)
        }
    }
}