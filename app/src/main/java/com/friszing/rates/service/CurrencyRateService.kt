package com.friszing.rates.service

import com.friszing.rates.model.CurrencyRateListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateService {

    @GET("android/latest")
    suspend fun fetchCurrencyRatesList(@Query("base") baseCurrency: String): CurrencyRateListResponse
}