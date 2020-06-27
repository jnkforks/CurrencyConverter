package com.friszing.rates.di.module

import android.content.SharedPreferences
import com.friszing.rates.currencycalculator.*
import com.friszing.rates.di.scope.CurrencyCalculatorScope
import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorBaseCurrencyDiffUtil
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorBaseCurrencyDiffUtilImpl
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorFragmentFactory
import com.friszing.rates.module.currencycalculator.mapper.*
import com.friszing.rates.module.currencycalculator.repository.CurrencyCalculatorRepository
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeBaseCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorChangeCalculationValueUseCase
import com.friszing.rates.module.currencycalculator.usecase.CurrencyCalculatorFetchCurrenciesUseCase
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named
import retrofit2.Retrofit

@Module
open class CurrencyCalculatorModule {

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyService(retrofit: Retrofit): CurrencyRateService =
        retrofit.create(CurrencyRateService::class.java)

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyRateListResponseMapper(): CurrencyRateListResponseMapper =
        CurrencyRateListResponseMapperImpl()

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyRateRepositoryConfiguration(
        @Named("RepositoryConfig")
        sharedPreferences: SharedPreferences
    ): CurrencyCalculatorConfiguration =
        CurrencyCalculatorConfigurationImpl(
            sharedPreferences
        )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyRateRepository(
        service: CurrencyRateService,
        responseMapper: CurrencyRateListResponseMapper,
        configuration: CurrencyCalculatorConfiguration
    ): CurrencyCalculatorRepository = CurrencyCalculatorRepositoryImpl(
        service,
        responseMapper,
        configuration
    )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyDetailProvider(): CurrencyDetailProvider =
        CurrencyDetailProviderImpl()

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyValueCalculator(): CurrencyValueCalculator =
        CurrencyValueCalculatorImpl()

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorItemListMapper(
        currencyDetailProvider: CurrencyDetailProvider,
        currencyValueCalculator: CurrencyValueCalculator
    ): CurrencyCalculatorItemListComposer =
        CurrencyCalculatorItemListComposerImpl(
            currencyDetailProvider,
            currencyValueCalculator
        )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorExceptionMapper(): CurrencyCalculatorExceptionMapper =
        CurrencyCalculatorExceptionMapperImpl()

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorChangeCalculationValueUseCase(
        configuration: CurrencyCalculatorConfiguration
    ): CurrencyCalculatorChangeCalculationValueUseCase =
        CurrencyCalculatorChangeCalculationValueUseCaseImpl(
            configuration
        )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorChangeBaseCalculationValueUseCase(
        configuration: CurrencyCalculatorConfiguration
    ): CurrencyCalculatorChangeBaseCalculationValueUseCase =
        CurrencyCalculatorChangeBaseCalculationValueUseCaseImpl(
            configuration
        )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorFetchCurrenciesUseCase(
        ratesRepository: CurrencyCalculatorRepository,
        configuration: CurrencyCalculatorConfiguration,
        currencyCalculatorItemListComposer: CurrencyCalculatorItemListComposer,
        currencyCalculatorExceptionMapper: CurrencyCalculatorExceptionMapper
    ): CurrencyCalculatorFetchCurrenciesUseCase = CurrencyCalculatorFetchCurrenciesUseCaseImpl(
        ratesRepository,
        configuration,
        currencyCalculatorItemListComposer,
        currencyCalculatorExceptionMapper
    )

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorFragmentViewModelFactory(
        fetchCurrenciesUseCase: CurrencyCalculatorFetchCurrenciesUseCase,
        changeCalculationValueUseCase: CurrencyCalculatorChangeCalculationValueUseCase,
        changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCase
    ) = CurrencyCalculatorFragmentViewModelFactory(
        fetchCurrenciesUseCase,
        changeCalculationValueUseCase,
        changeBaseCalculationValueUseCase
    )

    @CurrencyCalculatorScope
    @Provides
    open fun provideBaseCurrencyDiffUtil(): CurrencyCalculatorBaseCurrencyDiffUtil =
        CurrencyCalculatorBaseCurrencyDiffUtilImpl()

    @CurrencyCalculatorScope
    @Provides
    open fun provideCurrencyCalculatorFragmentFactory(
        currencyCalculatorFragmentViewModelFactory: CurrencyCalculatorFragmentViewModelFactory,
        currencyCalculatorBaseCurrencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil
    ) = CurrencyCalculatorFragmentFactory(
        currencyCalculatorFragmentViewModelFactory,
        currencyCalculatorBaseCurrencyDiffUtil
    )
}