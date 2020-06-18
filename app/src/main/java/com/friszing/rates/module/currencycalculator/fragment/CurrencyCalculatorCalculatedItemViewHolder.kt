package com.friszing.rates.module.currencycalculator.fragment

import android.widget.ImageView
import android.widget.TextView
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.google.android.material.textview.MaterialTextView

class CurrencyCalculatorCalculatedItemViewHolder(
    private val viewBinding: AdapterCurrencyItemBinding
) : CurrencyCalculatorItemViewHolder<MaterialTextView>(viewBinding.root) {

    override val title: TextView
        get() = viewBinding.title
    override val description: TextView
        get() = viewBinding.description
    override val currencyFlag: ImageView
        get() = viewBinding.currencyFlag
    override val amount: MaterialTextView
        get() = viewBinding.amount
}