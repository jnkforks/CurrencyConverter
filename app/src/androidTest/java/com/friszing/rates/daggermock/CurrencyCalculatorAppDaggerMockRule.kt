package com.friszing.rates.daggermock

import androidx.test.platform.app.InstrumentationRegistry
import com.friszing.rates.CurrencyCalculatorApp
import com.friszing.rates.di.component.CurrencyCalculatorAppComponent
import com.friszing.rates.di.module.CurrencyCalculatorAppModule
import com.friszing.rates.di.module.HttpModule
import com.friszing.rates.di.module.StorageModule
import it.cosenonjaviste.daggermock.DaggerMock

fun createCurrencyCalculatorAppDaggerMockRule() = DaggerMock.rule<CurrencyCalculatorAppComponent>(
    CurrencyCalculatorAppModule(app),
    HttpModule(),
    StorageModule()
) {
    set { component -> app.applicationComponent = component }
}

private val app: CurrencyCalculatorApp
    get() = InstrumentationRegistry.getInstrumentation()
        .targetContext.applicationContext as CurrencyCalculatorApp
