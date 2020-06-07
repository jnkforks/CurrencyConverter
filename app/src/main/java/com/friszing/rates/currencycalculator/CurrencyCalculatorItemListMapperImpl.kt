package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.mapper.CountryCodeMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyRateList
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem

class CurrencyCalculatorItemListMapperImpl(
    private val countryCodeMapper: CountryCodeMapper
) : CurrencyCalculatorItemListMapper {

    override fun map(
        currencyRateList: CurrencyRateList,
        calculationValue: Double
    ): List<CurrencyCalculatorItem> {
        val ratesItems = mutableListOf(
            CurrencyCalculatorItem(
                currencyRateList.baseCurrency,
                calculationValue,
                countryCodeMapper.map(currencyRateList.baseCurrency)
            )
        )
        ratesItems += currencyRateList.rates.map { currencyRate ->
            CurrencyCalculatorItem(
                currencyRate.key,
                currencyRate.value * calculationValue,
                countryCodeMapper.map(currencyRate.key)
            )
        }
        return ratesItems
    }
}