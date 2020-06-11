package com.friszing.rates.di.module

import android.content.SharedPreferences
import com.friszing.rates.currencycalculator.CurrencyCalculatorExceptionMapperImpl
import com.friszing.rates.currencycalculator.CurrencyCalculatorItemListMapperImpl
import com.friszing.rates.currencycalculator.CurrencyCalculatorRepositoryConfigurationImpl
import com.friszing.rates.currencycalculator.CurrencyCalculatorRepositoryImpl
import com.friszing.rates.currencycalculator.CurrencyDetailProviderImpl
import com.friszing.rates.currencycalculator.CurrencyRateListResponseMapperImpl
import com.friszing.rates.di.scope.CurrencyCalculatorScope
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorRepositoryConfiguration
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorBaseCurrencyDiffUtil
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorBaseCurrencyDiffUtilImpl
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentFactory
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorExceptionMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyDetailProvider
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Retrofit

@Module
class CurrencyCalculatorModule {

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyService(retrofit: Retrofit) =
        retrofit.create(CurrencyRateService::class.java)

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyRateListResponseMapper(): CurrencyRateListResponseMapper =
        CurrencyRateListResponseMapperImpl()

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyRateRepositoryConfiguration(
        @Named("RepositoryConfig")
        sharedPreferences: SharedPreferences
    ): CurrencyCalculatorRepositoryConfiguration =
        CurrencyCalculatorRepositoryConfigurationImpl(
            sharedPreferences
        )

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyRateRepository(
        service: CurrencyRateService,
        responseMapper: CurrencyRateListResponseMapper,
        currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper,
        repositoryConfiguration: CurrencyCalculatorRepositoryConfiguration
    ): CurrencyCalculatorRepository = CurrencyCalculatorRepositoryImpl(
        service,
        responseMapper,
        currencyCalculatorItemListMapper,
        repositoryConfiguration,
        IO
    )

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyDetailProvider(): CurrencyDetailProvider =
        CurrencyDetailProviderImpl()

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyCalculatorItemListMapper(
        currencyDetailProvider: CurrencyDetailProvider
    ): CurrencyCalculatorItemListMapper =
        CurrencyCalculatorItemListMapperImpl(currencyDetailProvider)

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyCalculatorExceptionMapper(): CurrencyCalculatorExceptionMapper =
        CurrencyCalculatorExceptionMapperImpl()

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyCalculatorFragmentViewModelFactory(
        ratesRepository: CurrencyCalculatorRepository,
        currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
    ) = CurrencyCalculatorFragmentViewModelFactory(
        ratesRepository,
        currencyCalculatorExceptionMapper
    )

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyCalculatorBaseCurrencyDiffUtil(): CurrencyCalculatorBaseCurrencyDiffUtil =
        CurrencyCalculatorBaseCurrencyDiffUtilImpl()

    @CurrencyCalculatorScope
    @Provides
    fun provideCurrencyCalculatorFragmentFactory(
        currencyCalculatorFragmentViewModelFactory: CurrencyCalculatorFragmentViewModelFactory,
        currencyCalculatorBaseCurrencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil
    ) = CurrencyCalculatorFragmentFactory(
        currencyCalculatorFragmentViewModelFactory,
        currencyCalculatorBaseCurrencyDiffUtil
    )
}