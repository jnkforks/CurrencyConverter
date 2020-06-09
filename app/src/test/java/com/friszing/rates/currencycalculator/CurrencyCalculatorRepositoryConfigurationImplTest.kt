package com.friszing.rates.currencycalculator

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorRepositoryConfigurationImplTest {

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @InjectMocks
    private lateinit var configuration: CurrencyCalculatorRepositoryConfigurationImpl

    @Test
    fun `Should provide the base currency from the shared preferences`() {
        // GIVEN
        whenever(sharedPreferences.getString(any(), any())).thenReturn("EUR")

        // WHEN
        val baseCurrency = configuration.baseCurrency

        // THEN
        assertThat(baseCurrency).isEqualTo("EUR")
    }

    @Test
    fun `Should save the base currency to the shared preferences`() {
        // GIVEN
        val editor = mock<SharedPreferences.Editor>()
        whenever(sharedPreferences.edit()).thenReturn(editor)
        // WHEN
        configuration.baseCurrency = "EUR"

        // THEN
        verify(editor).putString(
            CurrencyCalculatorRepositoryConfigurationImpl.KEY_CURRENCY,
            "EUR"
        )
        verify(editor).apply()
    }

    @Test
    fun `Should provide the request interval millis as 1000ms`() {
        // THEN
        assertThat(configuration.requestIntervalMillis).isEqualTo(1000L)
    }
}