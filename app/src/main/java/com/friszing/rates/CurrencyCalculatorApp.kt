package com.friszing.rates

import com.friszing.rates.di.component.DaggerCurrencyCalculatorAppComponent
import com.friszing.rates.di.module.CurrencyCalculatorAppModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CurrencyCalculatorApp : DaggerApplication(), HasAndroidInjector {

    var applicationComponent = DaggerCurrencyCalculatorAppComponent.builder()
        .currencyCalculatorAppModule(CurrencyCalculatorAppModule(this))
        .build()

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun applicationInjector(): AndroidInjector<out CurrencyCalculatorApp> =
        applicationComponent

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}