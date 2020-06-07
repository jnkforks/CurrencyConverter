package com.friszing.rates.currencycalculator

import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorGeneralException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorConnectionErrorException
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.lang.Exception

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
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorGeneralException(null))

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__general_error_message)
    }

    @Test
    fun `Should map the parse error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorParseException(null))

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__parsing_error_message)
    }

    @Test
    fun `Should map the connection error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyCalculatorConnectionErrorException(null))

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