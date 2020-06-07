package com.friszing.rates.view

import androidx.recyclerview.widget.RecyclerView
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.util.formatCurrency

class CurrencyRateItemsViewHolder(private val viewBinding: AdapterCurrencyItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(currencyRateItem: CurrencyRateItem) {
        bindName(currencyRateItem.name)
        bindValue(currencyRateItem.value)
    }

    fun bindName(name: String) {
        viewBinding.currencyName.text = name
    }

    fun bindValue(value: Double) = viewBinding.currencyValue.setText(value.formatCurrency())
}