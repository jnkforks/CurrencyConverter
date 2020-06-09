package com.friszing.rates.module.currencycalculator.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.databinding.AdapterCurrencyItemBinding
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail

typealias OnCurrencyCalculatorItemClick = (CurrencyCalculatorItem) -> Unit
typealias OnBaseCurrencyChanged = () -> Unit

class CurrencyCalculatorItemsAdapter(
    private val baseCurrencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil
) :
    Adapter<CurrencyCalculatorItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var currencyCalculatorItemClick: OnCurrencyCalculatorItemClick? = null
    private var baseCurrencyChanged: OnBaseCurrencyChanged? = null
    private val currencyRateItems = mutableListOf<CurrencyCalculatorItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyCalculatorItemViewHolder {
        val viewBinding =
            AdapterCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyCalculatorItemViewHolder(
            viewBinding,
            viewType == BASE_CURRENCY_VIEW_TYPE
        )
    }

    override fun getItemViewType(position: Int) =
        if (position == 0) BASE_CURRENCY_VIEW_TYPE else OTHER_CURRENCY_VIEW_TYPE

    override fun getItemId(position: Int): Long =
        currencyRateItems[position].currencyDetail.currencySymbol.hashCode().toLong()

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

    override fun onBindViewHolder(holder: CurrencyCalculatorItemViewHolder, position: Int) {
        val currencyRateItem = currencyRateItems[position]
        holder.bind(currencyRateItem)
        holder.itemView.setOnClickListener {
            currencyCalculatorItemClick?.invoke(
                currencyRateItem
            )
        }
    }

    fun onCurrencyCalculatorItemClickListener(currencyCalculatorItemClick: OnCurrencyCalculatorItemClick) {
        this.currencyCalculatorItemClick = currencyCalculatorItemClick
    }

    fun onBaseCurrencyChanged(baseCurrencyChanged: OnBaseCurrencyChanged) {
        this.baseCurrencyChanged = baseCurrencyChanged
    }

    fun setCurrencyRateItems(newList: List<CurrencyCalculatorItem>) {
        val isBaseCurrencyChanged =
            baseCurrencyDiffUtil.isBaseCurrenciesDifferent(currencyRateItems, newList)

        val diffResult = DiffUtil.calculateDiff(
            CurrencyCalculatorDiffUtil(
                currencyRateItems,
                newList
            )
        )

        diffResult.dispatchUpdatesTo(this)
        currencyRateItems.clear()
        currencyRateItems += newList

        if (isBaseCurrencyChanged)
            baseCurrencyChanged?.invoke()
    }

    companion object {
        const val KEY_VALUE = "KEY_VALUE"
        const val KEY_CURRENCY = "KEY_CURRENCY"
        const val BASE_CURRENCY_VIEW_TYPE = 1
        const val OTHER_CURRENCY_VIEW_TYPE = 2
    }
}