package com.friszing.rates.module.currencycalculator.fragment

import androidx.fragment.app.FragmentFactory
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyRatesFragmentViewModelFactory

class CurrencyRatesFragmentFactory(
    private val currencyRatesFragmentViewModelFactory: CurrencyRatesFragmentViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            CurrencyCalculatorFragment::class.qualifiedName -> CurrencyCalculatorFragment(
                currencyRatesFragmentViewModelFactory
            )
            else -> super.instantiate(classLoader, className)
        }
}