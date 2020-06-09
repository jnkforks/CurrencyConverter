package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.currencycalculator.CurrencyCalculatorItemListMapperImpl
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorItemListMapperImplTest {

    @Mock
    private lateinit var currencyDetailProvider: CurrencyDetailProvider

    @InjectMocks
    private lateinit var ratesItemListMapperImpl: CurrencyCalculatorItemListMapperImpl

    @Test
    fun `Should return the base currency as the first item of the currency rate items`() {
        // GIVEN
        val baseCurrency = "EUR"
        val calculationValue = 100.0
        val currencyRateList = CurrencyRateList(baseCurrency, mapOf("USD" to 1.2))
        val europeanUnion = CurrencyDetail("EUR", "EU", "EUflag")
        val unitedStates = CurrencyDetail("USD", "US", "USflag")

        setUpCountryCodeMapper("EUR", europeanUnion)
        setUpCountryCodeMapper("USD", unitedStates)

        // WHEN
        val currencyRateItems = ratesItemListMapperImpl.map(currencyRateList, calculationValue)

        // THEN
        assertThat(currencyRateItems.first()).isEqualTo(
            CurrencyCalculatorItem(
                europeanUnion,
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
        val europeanUnion = CurrencyDetail("EUR", "EU", "EU")
        val unitedStates = CurrencyDetail("USD", "US", "US")

        setUpCountryCodeMapper("EUR", europeanUnion)
        setUpCountryCodeMapper("USD", unitedStates)

        // WHEN
        val currencyRateItems = ratesItemListMapperImpl.map(currencyRateList, calculationValue)

        // THEN
        assertThat(currencyRateItems).isEqualTo(
            listOf(
                CurrencyCalculatorItem(
                    europeanUnion,
                    calculationValue
                ),
                CurrencyCalculatorItem(
                    unitedStates,
                    calculationValue * currencyRate
                )
            )
        )
    }

    @Test
    fun `Should map the currency code to country`() {
        // GIVEN
        val baseCurrency = "EUR"
        val calculationValue = 100.0
        val currencyRateList = CurrencyRateList(baseCurrency, mapOf("USD" to 1.2))
        val countryCodeMapperArgumentCaptor = argumentCaptor<String>()
        setUpCountryCodeMapper("EUR", mock())
        setUpCountryCodeMapper("USD", mock())

        // WHEN
        ratesItemListMapperImpl.map(currencyRateList, calculationValue)

        // THEN
        verify(currencyDetailProvider, times(2)).provide(countryCodeMapperArgumentCaptor.capture())
        assertThat(countryCodeMapperArgumentCaptor.allValues).isEqualTo(listOf("EUR", "USD"))
    }

    private fun setUpCountryCodeMapper(currencyCode: String, country: CurrencyDetail) {
        whenever(currencyDetailProvider.provide(currencyCode)).thenReturn(country)
    }
}