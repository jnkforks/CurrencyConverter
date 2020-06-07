package com.friszing.rates.view

import com.friszing.rates.R
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListGeneralException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListConnectionErrorException
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class CurrencyRatesExceptionMapperImplTest {

    private lateinit var currencyRatesExceptionMapperImpl: CurrencyRatesExceptionMapperImpl

    @Before
    fun setUp() {
        currencyRatesExceptionMapperImpl = CurrencyRatesExceptionMapperImpl()
    }

    @Test
    fun `Should map the general error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyRateListGeneralException(null))

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__general_error_message)
    }

    @Test
    fun `Should map the parse error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyRateListParseException(null))

        // THEN
        assertThat(errorMessageId).isEqualTo(R.string.rates_calculator__parsing_error_message)
    }

    @Test
    fun `Should map the connection error exception`() {
        // WHEN
        val errorMessageId =
            currencyRatesExceptionMapperImpl.map(CurrencyRateListConnectionErrorException(null))

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