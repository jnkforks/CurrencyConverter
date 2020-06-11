package com.friszing.rates.module.currencycalculator.fragment

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorItemsAdapter.Companion.KEY_CURRENCY
import com.friszing.rates.module.currencycalculator.fragment.CurrencyCalculatorItemsAdapter.Companion.KEY_VALUE
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

class CurrencyCalculatorDiffUtil(
    private val oldItems: List<CurrencyCalculatorItem>,
    private val newsItems: List<CurrencyCalculatorItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCurrencyDetail = oldItems[oldItemPosition].currencyDetail
        val newCurrencyDetail = newsItems[newItemPosition].currencyDetail
        return oldCurrencyDetail.currencySymbol == newCurrencyDetail.currencySymbol
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newsItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newsItems[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newCurrencyRateItem = newsItems[newItemPosition]
        val oldCurrencyRateItem = oldItems[oldItemPosition]
        val diffBundle = Bundle()

        if (newCurrencyRateItem.value != oldCurrencyRateItem.value) {
            diffBundle.putDouble(KEY_VALUE, newCurrencyRateItem.value)
        }

        if (newCurrencyRateItem.currencyDetail != oldCurrencyRateItem.currencyDetail) {
            diffBundle.putParcelable(KEY_CURRENCY, newCurrencyRateItem.currencyDetail)
        }

        return if (diffBundle.size() == 0) super.getChangePayload(
            oldItemPosition,
            newItemPosition
        ) else diffBundle
    }
}