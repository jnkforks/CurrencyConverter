package com.friszing.rates.service

import com.friszing.rates.exception.CurrencyRateListException.CurrencyRateListParseException
import com.friszing.rates.model.CurrencyRateList
import com.friszing.rates.model.CurrencyRateListResponse
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class CurrencyRateListResponseMapperImplTest {

    private lateinit var mapper: CurrencyRateListResponseMapperImpl

    @Before
    fun setUp() {
        mapper = CurrencyRateListResponseMapperImpl()
    }

    @Test
    fun `Should map the currency rate list response to the currency rate list`() {
        // WHEN
        val currencyRateListResponse =
            mapper.map(CurrencyRateListResponse("EUR", mapOf("USD" to 1.2)))

        // THEN
        assertThat(currencyRateListResponse).isEqualTo(CurrencyRateList("EUR", mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the given response is null`() {
        // WHEN
        mapper.map(null)
    }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the base currency is missing in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse(null, mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the rate list is missing in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("EUR", null))
    }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the base currency is empty in the response`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("", mapOf("USD" to 1.2)))
    }

    @Test(expected = CurrencyRateListParseException::class)
    fun `Should throw the parse exception when the rate list is empty`() {
        // WHEN
        mapper.map(CurrencyRateListResponse("EUR", emptyMap()))
    }
}