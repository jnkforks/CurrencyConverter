package com.friszing.rates.module.currencycalculator.fragment

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.utils.formatCurrency

typealias OnAmountChanged = (Double) -> Unit

class CurrencyCalculatorItemViewHolder(
    private val viewBinding: AdapterCurrencyItemBinding,
    private val isBaseCurrencyHolder: Boolean

) : RecyclerView.ViewHolder(viewBinding.root) {

    private var onAmountChanged: OnAmountChanged? = null

    init {
        configureAmountView()
    }

    fun bind(currencyCalculatorItem: CurrencyCalculatorItem) {
        bindCurrency(currencyCalculatorItem.currencyDetail)
        bindAmount(currencyCalculatorItem.value)
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

    fun setOnAmountChanged(onAmountChanged: OnAmountChanged) {
        this.onAmountChanged = onAmountChanged
    }

    fun bindAmount(value: Double) = viewBinding.amount.setText(value.formatCurrency())

    private fun configureAmountView() {
        viewBinding.amount.apply {
            isEnabled = isBaseCurrencyHolder
            if (isEnabled) {
                doAfterTextChanged {
                    onAmountChanged?.invoke(
                        it?.toString()?.replace(',', '.')
                            ?.toDoubleOrNull() ?: 0.0
                    )
                }
            }
        }
    }
}