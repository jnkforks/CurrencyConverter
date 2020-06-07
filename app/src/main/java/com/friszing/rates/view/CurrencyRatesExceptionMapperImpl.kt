package com.friszing.rates.view

import com.friszing.rates.R
import com.friszing.rates.exception.CurrencyRateListException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListGeneralException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListConnectionErrorException

class CurrencyRatesExceptionMapperImpl : CurrencyRatesExceptionMapper {

    override fun map(throwable: Throwable): Int =
        when (throwable) {
            is CurrencyRateListException -> mapHandledException(throwable)
            else -> R.string.rates_calculator__unhandled_error_message
        }

    private fun mapHandledException(currencyRateListException: CurrencyRateListException): Int =
        when (currencyRateListException) {
            is CurrencyRateListGeneralException -> R.string.rates_calculator__general_error_message
            is CurrencyRateListParseException -> R.string.rates_calculator__parsing_error_message
            is CurrencyRateListConnectionErrorException -> R.string.rates_calculator__network_error_message
        }
}