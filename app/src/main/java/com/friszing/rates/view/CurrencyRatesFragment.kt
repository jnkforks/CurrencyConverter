package com.friszing.rates.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.friszing.rates.databinding.FragmentCurrencyRatesBinding
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar

class CurrencyRatesFragment(
    private val currencyRatesFragmentViewModelFactory: CurrencyRatesFragmentViewModelFactory
) : Fragment() {

    private val currencyRatesFragmentViewModel: CurrencyRatesFragmentViewModel by viewModels(
        factoryProducer = { currencyRatesFragmentViewModelFactory }
    )

    private lateinit var viewBinding: FragmentCurrencyRatesBinding

    private val currencyRateItemsAdapter = CurrencyRateItemsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCurrencyRatesBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencyRatesFragmentViewModel.viewState.observe(
            viewLifecycleOwner,
            Observer(::onViewStateChange)
        )

        viewBinding.ratesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = currencyRateItemsAdapter
        }
    }

    private fun onViewStateChange(viewState: CurrencyRatesFragmentViewState) {
        viewState.error?.let {
            Snackbar.make(viewBinding.fragmentCurrencyRates, it, LENGTH_SHORT).show()
        }

        viewBinding.loadingIndicator.visibility = if (viewState.loading) VISIBLE else GONE

        currencyRateItemsAdapter.setCurrencyRateItems(viewState.items)
    }
}