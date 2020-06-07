package com.friszing.rates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.friszing.rates.databinding.AdapterCurrencyItemBinding

class CurrencyRateItemsAdapter : Adapter<CurrencyRateItemsViewHolder>() {

    private var currencyRateItems = emptyList<CurrencyRateItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateItemsViewHolder {
        val viewBinding =
            AdapterCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyRateItemsViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = currencyRateItems.size

    override fun onBindViewHolder(
        holder: CurrencyRateItemsViewHolder,
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

    override fun onBindViewHolder(holder: CurrencyRateItemsViewHolder, position: Int) =
        holder.bind(currencyRateItems[position])

    fun setCurrencyRateItems(newList: List<CurrencyRateItem>) {
        val diffResult = DiffUtil.calculateDiff(CurrencyItemsDiffUtils(currencyRateItems, newList))
        diffResult.dispatchUpdatesTo(this)
        currencyRateItems = newList
    }

    companion object {
        const val KEY_NAME = "KEY_NAME"
        const val KEY_VALUE = "KEY_VALUE"
    }
}