package com.friszing.rates.util

import com.friszing.rates.utils.formatCurrency
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CurrencyFormatterExtKtTest {

    @Test
    fun `Should format the double value to the string`() {
        // WHEN
        val formattedValue = (100.0).formatCurrency()

        assertThat(formattedValue).isEqualTo("100.00")
    }
}