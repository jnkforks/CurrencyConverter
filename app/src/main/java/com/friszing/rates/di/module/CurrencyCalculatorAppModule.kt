package com.friszing.rates.di.module

import android.app.Application
import com.friszing.rates.CurrencyCalculatorApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class CurrencyCalculatorAppModule(private val currencyCalculatorApp: CurrencyCalculatorApp) {

    @Singleton
    @Provides
    open fun providesApplication(): Application = currencyCalculatorApp
}