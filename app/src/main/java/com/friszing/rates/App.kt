package com.friszing.rates

import com.friszing.rates.di.component.DaggerCurrencyCalculatorAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : DaggerApplication(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun applicationInjector(): AndroidInjector<out App> =
        DaggerCurrencyCalculatorAppComponent.builder()
            .application(this)
            .build()


    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}