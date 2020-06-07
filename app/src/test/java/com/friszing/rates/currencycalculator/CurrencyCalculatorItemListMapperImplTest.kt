package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class CurrencyCalculatorItemListMapperImplTest {

    private lateinit var ratesItemListMapperImpl: CurrencyCalculatorItemListMapperImpl

    @Before
    fun setUp() {
        ratesItemListMapperImpl =
            CurrencyCalculatorItemListMapperImpl()
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
            CurrencyCalculatorItem(
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
                CurrencyCalculatorItem(
                    baseCurrency,
                    calculationValue
                ),
                CurrencyCalculatorItem(
                    currency,
                    calculationValue * currencyRate
                )
            )
        )
    }
}