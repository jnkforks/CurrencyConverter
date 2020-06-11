package com.friszing.rates.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
open class HttpModule {

    @Singleton
    @Provides
    open fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    open fun provideMoshiConverterFactory(
        moshi: Moshi
    ): MoshiConverterFactory =
        MoshiConverterFactory
            .create(moshi)

    @Singleton
    @Provides
    open fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://hiring.revolut.codes/api/")
        .addConverterFactory(moshiConverterFactory)
        .build()
}