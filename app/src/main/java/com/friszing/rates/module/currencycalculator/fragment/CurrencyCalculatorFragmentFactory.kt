package com.friszing.rates.module.currencycalculator.fragment

import androidx.fragment.app.FragmentFactory
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory

class CurrencyCalculatorFragmentFactory(
    private val currencyCalculatorFragmentViewModelFactory: CurrencyCalculatorFragmentViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            CurrencyCalculatorFragment::class.qualifiedName -> CurrencyCalculatorFragment(
                currencyCalculatorFragmentViewModelFactory
            )
            else -> super.instantiate(classLoader, className)
        }
}