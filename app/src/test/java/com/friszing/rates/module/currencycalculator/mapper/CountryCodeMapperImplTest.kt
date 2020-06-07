package com.friszing.rates.module.currencycalculator.mapper

import com.friszing.rates.module.currencycalculator.model.Country
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CountryCodeMapperImplTest {

    @Mock
    private lateinit var currencyCountryProvider: CurrencyCountryProvider

    @InjectMocks
    private lateinit var countryCodeMapper: CountryCodeMapperImpl


    @Test
    fun `Should return the country by the given country code`() {
        // GIVEN
        val expectedCountry = mock<Country>()
        setUpCurrencyCountryProvider(expectedCountry)

        // WHEN
        val country = countryCodeMapper.map("EUR")

        // THEN
        assertThat(country).isEqualTo(expectedCountry)
    }

    @Test
    fun `Should return null when there is no country with the given country code`() {
        // GIVEN
        setUpCurrencyCountryProvider(null)

        // WHEN
        val country = countryCodeMapper.map("EUR")

        // THEN
        assertThat(country).isEqualTo(null)
    }

    private fun setUpCurrencyCountryProvider(country: Country?) {
        val countries = mock<Map<String, Country>>()
        whenever(countries[any()]).thenReturn(country)
        whenever(currencyCountryProvider.currencyCountriesMap).thenReturn(countries)
    }
}