package com.friszing.rates.di.component

import android.app.Application
import com.friszing.rates.App
import com.friszing.rates.di.module.AndroidBindingModule
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
        StorageModule::class,
        AndroidBindingModule::class
    ]
)
interface CurrencyCalculatorAppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): CurrencyCalculatorAppComponent
    }
}