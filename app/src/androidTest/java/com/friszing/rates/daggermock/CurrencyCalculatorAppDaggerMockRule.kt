package com.friszing.rates.daggermock

import androidx.test.platform.app.InstrumentationRegistry
import com.friszing.rates.CurrencyCalculatorApp
import com.friszing.rates.di.component.CurrencyCalculatorAppComponent
import it.cosenonjaviste.daggermock.DaggerMock

fun createCurrencyCalculatorAppDaggerMockRule() =
    DaggerMock.rule<CurrencyCalculatorAppComponent> {
        customizeBuilder<CurrencyCalculatorAppComponent.Builder> { b -> b.application(app) }
        set { component -> app.applicationComponent = component }
    }

private val app: CurrencyCalculatorApp
    get() = InstrumentationRegistry.getInstrumentation()
        .targetContext.applicationContext as CurrencyCalculatorApp