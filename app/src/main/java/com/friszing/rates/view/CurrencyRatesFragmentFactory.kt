package com.friszing.rates.view

import androidx.fragment.app.FragmentFactory

class CurrencyRatesFragmentFactory(
    private val currencyRatesFragmentViewModelFactory: CurrencyRatesFragmentViewModelFactory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {
            CurrencyRatesFragment::class.qualifiedName -> CurrencyRatesFragment(
                currencyRatesFragmentViewModelFactory
            )
            else -> super.instantiate(classLoader, className)
        }
}