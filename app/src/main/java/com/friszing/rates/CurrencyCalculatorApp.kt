package com.friszing.rates

import com.friszing.rates.di.component.CurrencyCalculatorAppComponent
import com.friszing.rates.di.component.DaggerCurrencyCalculatorAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CurrencyCalculatorApp : DaggerApplication(), HasAndroidInjector {

    lateinit var applicationComponent: CurrencyCalculatorAppComponent

    override fun onCreate() {
        applicationComponent = DaggerCurrencyCalculatorAppComponent.builder()
            .application(this)
            .build()
        super.onCreate()
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun applicationInjector(
    ): AndroidInjector<out CurrencyCalculatorApp> = applicationComponent

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}