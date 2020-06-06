package com.friszing.rates.di.module

import android.content.SharedPreferences
import com.friszing.rates.configuration.CurrencyRateRepositoryConfiguration
import com.friszing.rates.configuration.CurrencyRateRepositoryConfigurationImpl
import com.friszing.rates.repository.CurrencyRateRepository
import com.friszing.rates.repository.CurrencyRateRepositoryImpl
import com.friszing.rates.service.CurrencyRateListResponseMapper
import com.friszing.rates.service.CurrencyRateListResponseMapperImpl
import com.friszing.rates.service.CurrencyRateService
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

class RatesModule {

    fun provideCurrencyService(retrofit: Retrofit) =
        retrofit.create(CurrencyRateService::class.java)

    fun provideCurrencyRateListResponseMapper(): CurrencyRateListResponseMapper =
        CurrencyRateListResponseMapperImpl()

    fun provideCurrencyRateRepositoryConfiguration(
        sharedPreferences: SharedPreferences
    ): CurrencyRateRepositoryConfiguration =
        CurrencyRateRepositoryConfigurationImpl(sharedPreferences)

    fun provideCurrencyRateRepository(
        service: CurrencyRateService,
        mapper: CurrencyRateListResponseMapper,
        repositoryConfiguration: CurrencyRateRepositoryConfiguration,
        coroutineDispatcher: CoroutineDispatcher
    ): CurrencyRateRepository =
        CurrencyRateRepositoryImpl(
            service,
            mapper,
            repositoryConfiguration,
            coroutineDispatcher
        )

}