package com.friszing.rates.di.component

import com.friszing.rates.CurrencyCalculatorApp
import com.friszing.rates.di.module.CurrencyCalculatorAppModule
import com.friszing.rates.di.module.HttpModule
import com.friszing.rates.di.module.StorageModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        CurrencyCalculatorAppModule::class,
        HttpModule::class,
        StorageModule::class
    ]
)
interface CurrencyCalculatorAppComponent : AndroidInjector<CurrencyCalculatorApp> {

    fun currencyCalculatorComponentBuilder(): CurrencyCalculatorComponent.Builder

    @Component.Builder
    interface Builder {

        fun httpModule(httpModule: HttpModule): Builder

        fun storageModule(storageModule: StorageModule): Builder

        fun currencyCalculatorAppModule(
            currencyCalculatorAppModule: CurrencyCalculatorAppModule
        ): Builder

        fun build(): CurrencyCalculatorAppComponent
    }
}