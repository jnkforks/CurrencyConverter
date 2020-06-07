package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse

interface CurrencyRateListResponseMapper {
    @Throws(CurrencyCalculatorParseException::class)
    fun map(currencyRateListResponse: CurrencyRateListResponse?): CurrencyRateList
}