package com.friszing.rates.module.currencycalculator.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.util.formatCurrency

class CurrencyCalculatorItemViewHolder(
    private val viewBinding: AdapterCurrencyItemBinding,
    private val isBaseCurrencyHolder: Boolean
) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(currencyCalculatorItem: CurrencyCalculatorItem) {
        viewBinding.amount.isEnabled = isBaseCurrencyHolder
        bindAmount(currencyCalculatorItem.value)
        bindCurrency(currencyCalculatorItem.currencyDetail)
    }

    fun bindCurrency(country: CurrencyDetail) {
        viewBinding.title.text = country.currencySymbol
        viewBinding.description.text = country.currencyDescription
        Glide.with(viewBinding.root)
            .load(country.currencyFlagUrl)
            .apply(RequestOptions().circleCrop())
            .into(viewBinding.currencyFlag)
        viewBinding.currencyFlag.contentDescription = country.currencyDescription
    }

    fun bindAmount(value: Double) = viewBinding.amount.setText(value.formatCurrency())
}