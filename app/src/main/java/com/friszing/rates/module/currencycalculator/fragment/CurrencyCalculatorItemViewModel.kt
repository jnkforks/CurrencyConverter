package com.friszing.rates.module.currencycalculator.fragment

import androidx.recyclerview.widget.RecyclerView
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.util.formatCurrency

class CurrencyCalculatorItemViewModel(private val viewBinding: AdapterCurrencyItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(currencyCalculatorItem: CurrencyCalculatorItem) {
        bindName(currencyCalculatorItem.currencyCode)
        bindValue(currencyCalculatorItem.value)
    }

    fun bindName(name: String) {
        viewBinding.currencyName.text = name
    }

    fun bindValue(value: Double) = viewBinding.currencyValue.setText(value.formatCurrency())
}