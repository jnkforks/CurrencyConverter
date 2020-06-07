package com.friszing.rates.module.currencycalculator.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail

class CurrencyCalculatorItemsAdapter : Adapter<CurrencyCalculatorItemViewHolder>() {

    private val currencyRateItems = mutableListOf<CurrencyCalculatorItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyCalculatorItemViewHolder {
        val viewBinding =
            AdapterCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyCalculatorItemViewHolder(
            viewBinding
        )
    }

    override fun getItemCount(): Int = currencyRateItems.size

    override fun onBindViewHolder(
        holder: CurrencyCalculatorItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)

        val bundle =
            payloads.first() as? Bundle ?: return super.onBindViewHolder(holder, position, payloads)


        bundle.getParcelable<CurrencyDetail>(KEY_CURRENCY)?.let(holder::bindCurrency)

        if (bundle.containsKey(KEY_VALUE)) {
            holder.bindAmount(bundle.getDouble(KEY_VALUE))
        }
    }

    override fun onBindViewHolder(holder: CurrencyCalculatorItemViewHolder, position: Int) =
        holder.bind(currencyRateItems[position])

    fun setCurrencyRateItems(newList: List<CurrencyCalculatorItem>) {
        val diffResult = DiffUtil.calculateDiff(
            CurrencyCalculatorDiffUtil(
                currencyRateItems,
                newList
            )
        )
        diffResult.dispatchUpdatesTo(this)
        currencyRateItems.clear()
        currencyRateItems += newList
    }

    companion object {
        const val KEY_VALUE = "KEY_VALUE"
        const val KEY_CURRENCY = "KEY_CURRENCY"
    }
}