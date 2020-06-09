package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.R
import com.friszing.rates.currencycalculator.CurrencyCalculatorExceptionMapperImpl
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CurrencyCalculatorExceptionMapperImplTest {

    private lateinit var currencyRatesExceptionMapperImpl: CurrencyCalculatorExceptionMapperImpl

    @Before
    fun setUp() {
        currencyRatesExceptionMapperImpl =
            CurrencyCalculatorExceptionMapperImpl()
    }

    @Test
    fun `Should map the general error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorGeneralException())

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__general_error_message)
    }

    @Test
    fun `Should map the parse error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorParseException())

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__parsing_error_message)
    }

    @Test
    fun `Should map the connection error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorConnectionErrorException())

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__network_error_message)
    }

    @Test
    fun `Should map the unhandled error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(Exception())

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__unhandled_error_message)
    }
}