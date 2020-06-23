package com.friszing.rates.di.component

import android.app.Application
import com.friszing.rates.CurrencyCalculatorApp
import com.friszing.rates.di.module.HttpModule
import com.friszing.rates.di.module.StorageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        HttpModule::class,
        StorageModule::class
    ]
)
interface CurrencyCalculatorAppComponent : AndroidInjector<CurrencyCalculatorApp> {

    fun currencyCalculatorComponentBuilder(): CurrencyCalculatorComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): CurrencyCalculatorAppComponent
    }
}