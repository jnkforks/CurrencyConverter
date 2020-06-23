package com.friszing.rates.module.currencycalculator.mapper

typealias CurrencyRate = Map.Entry<String, Double>

interface CurrencyValueCalculator {
    fun calculate(currencyRate: CurrencyRate, calculationValue: Double): Double
}