package com.friszing.rates.module.currencycalculator.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyDetail(
    val currencySymbol: String,
    val currencyDescription: String,
    val currencyFlagUrl: String
) : Parcelable