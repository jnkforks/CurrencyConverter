package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.currencycalculator.CurrencyRateListResponseMapperImpl
import com.friszing.rates.module.currencycalculator.exception.CurrencyCalculatorException.CurrencyCalculatorParseException
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CurrencyRateListResponseMapperImplTest {

    private lateinit var mapper: CurrencyRateListResponseMapperImpl

    @Before
    fun setUp() {
        mapper =
            CurrencyRateListResponseMapperImpl()
    }

    @Test
    fun `Should map the currency rate list response to the currency rate list`() {
        // WHEN
        val currencyRateListResponse =
            mapper.map(CurrencyRateListResponse("EUR", mapOf("USD" to 1.2)))

        // THEN
        assertThat(currencyRateListResponse).isEqualTo(CurrencyRateList("EUR", mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the given response is null`() {
        // WHEN
        mapper.map(null)
    }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the base currency is missing in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse(null, mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the rate list is missing in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("EUR", null))
    }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the base currency is empty in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("", mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyCalculatorParseException::class)
    fun `Should throw the parse exception when the rate list is empty`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("EUR", emptyMap()))
    }
}