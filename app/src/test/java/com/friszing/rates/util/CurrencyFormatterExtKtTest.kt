package com.friszing.rates.util

import com.friszing.rates.utils.formatCurrency
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyFormatterExtKtTest {

    @Test
    fun `Should format double to 2 decimal string`() {
        // WHEN
        val formattedValue = (100.20).formatCurrency()

        assertThat(formattedValue).isEqualTo("100.20")
    }

    @Test
    fun `Should use round up scaling`() {
        // WHEN
        val formattedValue = (100.199).formatCurrency()

        assertThat(formattedValue).isEqualTo("100.20")
    }

}