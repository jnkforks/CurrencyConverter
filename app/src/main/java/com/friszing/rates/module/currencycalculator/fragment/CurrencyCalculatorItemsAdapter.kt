package com.friszing.rates.module.currencycalculator.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.databinding.AdapterCurrencyItemBinding

class CurrencyCalculatorItemsAdapter : Adapter<CurrencyCalculatorItemViewModel>() {

    private var currencyRateItems = emptyList<CurrencyCalculatorItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyCalculatorItemViewModel {
        val viewBinding =
            AdapterCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyCalculatorItemViewModel(
            viewBinding
        )
    }

    override fun getItemCount(): Int = currencyRateItems.size

    override fun onBindViewHolder(
        holder: CurrencyCalculatorItemViewModel,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)


        val bundle =
            payloads.first() as? Bundle ?: return super.onBindViewHolder(holder, position, payloads)

        bundle.getString(KEY_NAME)?.let { holder.bindName(it) }

        if (bundle.containsKey(KEY_VALUE)) {
            holder.bindValue(bundle.getDouble(KEY_VALUE))
        }
    }

    override fun onBindViewHolder(holder: CurrencyCalculatorItemViewModel, position: Int) =
        holder.bind(currencyRateItems[position])

    fun setCurrencyRateItems(newList: List<CurrencyCalculatorItem>) {
        val diffResult = DiffUtil.calculateDiff(
            CurrencyCalculatorDiffUtil(
                currencyRateItems,
                newList
            )
        )
        diffResult.dispatchUpdatesTo(this)
        currencyRateItems = newList
    }

    companion object {
        const val KEY_NAME = "KEY_NAME"
        const val KEY_VALUE = "KEY_VALUE"
    }
}