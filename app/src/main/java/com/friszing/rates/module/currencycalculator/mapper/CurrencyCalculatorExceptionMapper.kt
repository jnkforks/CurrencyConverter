package com.friszing.rates.module.currencycalculator.mapper

interface CurrencyCalculatorExceptionMapper {
    fun map(throwable: Throwable): Int
}
