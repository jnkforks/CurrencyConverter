package com.friszing.rates.utils

import java.math.BigDecimal
import java.math.BigDecimal.ROUND_UP
import java.text.DecimalFormat

fun Double.formatCurrency(): String = DecimalFormat("0.00")
    .format(
        BigDecimal(this).apply {
            setScale(2, ROUND_UP)
        }
    )
