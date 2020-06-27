package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.currencycalculator.CurrencyCalculatorItemListComposerImpl
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mock

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorItemListComposerImplTest {

    @Mock
    private lateinit var currencyDetailProvider: CurrencyDetailProvider

    @Mock
    private lateinit var currencyValueCalculator: CurrencyValueCalculator

    @InjectMocks
    private lateinit var ratesItemListMapperImpl: CurrencyCalculatorItemListComposerImpl

    @Before
    fun setUp() {
        setUpCurrencyValueCalculator()
    }

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
        val currencyRateItems = ratesItemListMapperImpl.apply(currencyRateList, calculationValue)

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
        val currencyRateItems = ratesItemListMapperImpl.apply(currencyRateList, calculationValue)

        // THEN
        assertThat(currencyRateItems).isEqualTo(
            listOf(
                CurrencyCalculatorItem(
                    europeanUnion,
                    calculationValue
                ),
                CurrencyCalculatorItem(
                    unitedStates,
                    0.0
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
        ratesItemListMapperImpl.apply(currencyRateList, calculationValue)

        // THEN
        verify(currencyDetailProvider, times(2)).provide(countryCodeMapperArgumentCaptor.capture())
        assertThat(countryCodeMapperArgumentCaptor.allValues).isEqualTo(listOf("EUR", "USD"))
    }

    @Test
    fun `Should not map when the base currency is blank`() {
        // WHEN
        val currencyList =
            ratesItemListMapperImpl.apply(CurrencyRateList("", mapOf("USD" to 1.2)), 0.0)

        // THEN
        assertThat(currencyList).isEmpty()
    }

    private fun setUpCountryCodeMapper(currencyCode: String, country: CurrencyDetail) {
        whenever(currencyDetailProvider.provide(currencyCode)).thenReturn(country)
    }

    private fun setUpCurrencyValueCalculator() {
        whenever(currencyValueCalculator.calculate(any(), any())).thenReturn(0.0)
    }
}