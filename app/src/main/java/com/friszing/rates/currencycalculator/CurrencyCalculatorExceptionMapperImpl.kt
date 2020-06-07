package com.friszing.rates.currencycalculator

import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper

class CurrencyCalculatorExceptionMapperImpl :
    CurrencyCalculatorExceptionMapper {

    override fun map(throwable: Throwable): Int =
        when (throwable) {
            is CurrencyCalculatorException -> mapHandledException(throwable)
            else -> R.string.rates_calculator__unhandled_error_message
        }

    private fun mapHandledException(currencyCalculatorException: CurrencyCalculatorException): Int =
        when (currencyCalculatorException) {
            is CurrencyCalculatorGeneralException -> R.string.rates_calculator__general_error_message
            is CurrencyCalculatorParseException -> R.string.rates_calculator__parsing_error_message
            is CurrencyCalculatorConnectionErrorException -> R.string.rates_calculator__network_error_message
        }
}