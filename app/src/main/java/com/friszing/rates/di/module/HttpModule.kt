package com.friszing.rates.di.module

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class HttpModule {

    fun provideMoshiConverterFactory() = MoshiConverterFactory.create()

    fun provideRetrofit(moshiConverterFactory: MoshiConverterFactory) =
        Retrofit.Builder()
            .baseUrl("https://hiring.revolut.codes/api/")
            .addConverterFactory(MoshiConverterFactory.create())

}