package com.friszing.rates.view

interface CurrencyRatesExceptionMapper {
    fun map(throwable: Throwable): Int
}
