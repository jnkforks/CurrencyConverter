package com.friszing.rates.di.module

import com.friszing.rates.MainActivity
import com.friszing.rates.di.scope.CurrencyCalculatorScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBindingModule {

    @ContributesAndroidInjector(
        modules = [CurrencyCalculatorModule::class]
    )
    @CurrencyCalculatorScope
    abstract fun mainActivity(): MainActivity
}