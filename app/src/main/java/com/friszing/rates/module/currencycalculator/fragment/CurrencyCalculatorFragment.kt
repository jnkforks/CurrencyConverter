package com.friszing.rates.module.currencycalculator.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friszing.rates.R
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModel
import com.friszing.rates.module.currencycalculator.viewmodel.CurrencyCalculatorFragmentViewModelFactory
import com.friszing.rates.databinding.FragmentCurrencyRatesBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class CurrencyCalculatorFragment(
    private val currencyCalculatorFragmentViewModelFactory: CurrencyCalculatorFragmentViewModelFactory,
    currencyDiffUtil: CurrencyCalculatorBaseCurrencyDiffUtil
) : Fragment() {

    private val currencyCalculatorFragmentViewModel: CurrencyCalculatorFragmentViewModel by viewModels(
        factoryProducer = { currencyCalculatorFragmentViewModelFactory }
    )

    private lateinit var viewBinding: FragmentCurrencyRatesBinding

    private val errorSnackbar: Snackbar by lazy {
        Snackbar.make(
            viewBinding.root,
            R.string.rates_calculator__general_error_message,
            LENGTH_INDEFINITE
        )
    }

    private val currencyRateItemsAdapter =
        CurrencyCalculatorItemsAdapter(currencyDiffUtil)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCurrencyRatesBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyCalculatorFragmentViewModel.viewState.observe(
            viewLifecycleOwner,
            Observer(::onViewStateChange)
        )
        val linearLayoutManager = LinearLayoutManager(requireContext())

        viewBinding.ratesRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = currencyRateItemsAdapter
        }

        currencyRateItemsAdapter.onCurrencyCalculatorItemClickListener {
            currencyCalculatorFragmentViewModel.onCurrencyItemClicked(it)
        }

        currencyRateItemsAdapter.onBaseCurrencyChanged {
            viewBinding.ratesRecyclerView.smoothScrollToPosition(0)
        }
    }

    private fun onViewStateChange(viewState: CurrencyCalculatorFragmentViewState) {
        viewState.error?.let {
            errorSnackbar.setText(it).show()
        } ?: errorSnackbar.dismiss()

        viewBinding.loadingIndicator.visibility = if (viewState.loading) VISIBLE else GONE

        currencyRateItemsAdapter.setCurrencyRateItems(viewState.items)
    }
}