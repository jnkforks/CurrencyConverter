package com.friszing.rates.module.currencycalculator.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.friszing.rates.utils.formatCurrency


abstract class CurrencyCalculatorItemViewHolder<out T : TextView>(
    view: View
) : RecyclerView.ViewHolder(view) {

    abstract val title: TextView

    abstract val description: TextView

    abstract val currencyFlag: ImageView

    abstract val amount: T

    fun bind(currencyCalculatorItem: CurrencyCalculatorItem) {
        bindCurrencyDetail(currencyCalculatorItem.currencyDetail)
        bindAmount(currencyCalculatorItem.value)
    }

    fun bindCurrencyDetail(currencyDetail: CurrencyDetail) {
        title.text = currencyDetail.currencySymbol
        description.text = currencyDetail.currencyDescription
        Glide.with(itemView)
            .load(currencyDetail.currencyFlagUrl)
            .apply(RequestOptions().circleCrop())
            .into(currencyFlag)
        currencyFlag.contentDescription = currencyDetail.currencyDescription
    }

    fun bindAmount(value: Double) = amount.setText(value.formatCurrency())
}