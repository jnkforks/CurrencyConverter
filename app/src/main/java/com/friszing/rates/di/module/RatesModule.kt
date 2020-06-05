package com.friszing.rates.di.module

import com.friszing.rates.service.CurrencyRateService
import retrofit2.Retrofit

class RatesModule {

    fun provideCurrencyService(retrofit: Retrofit) =
        retrofit.create(CurrencyRateService::class.java)

}