package com.friszing.rates.di.component

import com.friszing.rates.CurrencyCalculatorActivity
import com.friszing.rates.di.module.CurrencyCalculatorModule
import com.friszing.rates.di.scope.CurrencyCalculatorScope
import dagger.Subcomponent
import dagger.android.AndroidInjector

@CurrencyCalculatorScope
@Subcomponent(modules = [CurrencyCalculatorModule::class])
interface CurrencyCalculatorComponent : AndroidInjector<CurrencyCalculatorActivity> {

    @Subcomponent.Builder
    interface Builder {
        fun currencyCalculatorModule(module: CurrencyCalculatorModule): Builder
        fun build(): CurrencyCalculatorComponent
    }
}