package com.friszing.rates.view

import com.friszing.rates.model.CurrencyRateList
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class CurrencyRatesItemListMapperImplTest {

    private lateinit var ratesItemListMapperImpl: CurrencyRatesItemListMapperImpl

    @Before
    fun setUp() {
        ratesItemListMapperImpl = CurrencyRatesItemListMapperImpl()
    }

    @Test
    fun `Should return the base currency as the first item of the currency rate items`() {
        // GIVEN
        val baseCurrency = "EUR"
        val calculationValue = 100.0
        val currencyRateList = CurrencyRateList(baseCurrency, mapOf("USD" to 1.2))

        // WHEN
        val currencyRateItems = ratesItemListMapperImpl.map(currencyRateList, calculationValue)

        // THEN
        assertThat(currencyRateItems.first()).isEqualTo(
            CurrencyRateItem(
                baseCurrency,
                calculationValue
            )
        )
    }

    @Test
    fun `Should return the currency rate items with the given calculation value`() {
        // GIVEN
        val baseCurrency = "EUR"
        val calculationValue = 100.0
        val currency = "USD"
        val currencyRate = 1.2
        val currencyRateList = CurrencyRateList(baseCurrency, mapOf(currency to currencyRate))

        // WHEN
        val currencyRateItems = ratesItemListMapperImpl.map(currencyRateList, calculationValue)

        // THEN
        assertThat(currencyRateItems).isEqualTo(
            listOf(
                CurrencyRateItem(
                    baseCurrency,
                    calculationValue
                ),
                CurrencyRateItem(
                    currency,
                    calculationValue * currencyRate
                )
            )
        )
    }
}