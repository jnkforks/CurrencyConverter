package com.friszing.rates.module.currencycalculator.fragment

import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.friszing.rates.databinding.AdapterCurrencyBaseItemBinding
import com.google.android.material.textfield.TextInputEditText

typealias OnAmountChanged = (Double) -> Unit

class CurrencyCalculatorBaseItemViewHolder(
    private val binding: AdapterCurrencyBaseItemBinding
) : CurrencyCalculatorItemViewHolder<TextInputEditText>(binding.root) {

    override val amount: TextInputEditText
        get() = binding.amount
    override val title: TextView
        get() = binding.title
    override val description: TextView
        get() = binding.description
    override val currencyFlag: ImageView
        get() = binding.currencyFlag

    private var onAmountChanged: OnAmountChanged? = null

    init {
        amount.doAfterTextChanged {
            onAmountChanged?.invoke(
                it?.toString()?.replace(',', '.')
                    ?.toDoubleOrNull() ?: 0.0
            )
        }
    }

    fun setOnAmountChanged(onAmountChanged: OnAmountChanged) {
        this.onAmountChanged = onAmountChanged
    }
}