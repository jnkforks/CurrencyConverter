package com.friszing.rates.di.module

import android.content.SharedPreferences
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration
import com.friszing.rates.configuration.CurrencyCalculatorRepositoryConfigurationImpl
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.currencycalculator.CurrencyCalculatorRepositoryImpl
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.currencycalculator.CurrencyRateListResponseMapperImpl
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

class RatesModule {

    fun provideCurrencyService(retrofit: Retrofit) =
        retrofit.create(CurrencyRateService::class.java)

    fun provideCurrencyRateListResponseMapper(): CurrencyRateListResponseMapper =
        CurrencyRateListResponseMapperImpl()

    fun provideCurrencyRateRepositoryConfiguration(
        sharedPreferences: SharedPreferences
    ): CurrencyCalculatorRepositoryConfiguration =
        CurrencyCalculatorRepositoryConfigurationImpl(sharedPreferences)

    fun provideCurrencyRateRepository(
        service: CurrencyRateService,
        mapper: CurrencyRateListResponseMapper,
        repositoryConfiguration: CurrencyCalculatorRepositoryConfiguration,
        coroutineDispatcher: CoroutineDispatcher
    ): CurrencyCalculatorRepository =
        CurrencyCalculatorRepositoryImpl(
            service,
            mapper,
            repositoryConfiguration,
            coroutineDispatcher
        )

}