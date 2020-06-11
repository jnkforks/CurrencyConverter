package com.friszing.rates.module.currencycalculator.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyCalculatorItem(
    val currencyDetail: CurrencyDetail,
    val value: Double
) : Parcelable