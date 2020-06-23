package com.friszing.rates.currencycalculator

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class CurrencyValueCalculatorImplTest {

    @Test
    fun `Should calculate currency value`() {
        // GIVEN
        val currencyCalculator = CurrencyValueCalculatorImpl()

        // WHEN
        val actualCalculationValue = currencyCalculator.calculate(
            mapOf("USD" to 1.1).entries.first(),
            10.0
        )
        assertThat(actualCalculationValue).isEqualTo(11.0)
    }
}