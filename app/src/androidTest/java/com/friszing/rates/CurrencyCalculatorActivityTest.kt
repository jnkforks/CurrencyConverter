package com.friszing.rates

import androidx.test.rule.ActivityTestRule
import com.friszing.rates.daggermock.createCurrencyCalculatorAppDaggerMockRule
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.friszing.rates.utils.ResponseUtils
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class CurrencyCalculatorActivityTest {

    @get:Rule
    val activityRule =
        ActivityTestRule(CurrencyCalculatorActivity::class.java, true, false)

    @get:Rule
    val currencyCalculatorAppDaggerMockRule = createCurrencyCalculatorAppDaggerMockRule()

    @Mock
    private lateinit var service: CurrencyRateService

    private val responseUtils = ResponseUtils()

    @ExperimentalCoroutinesApi
    @Test
    fun exampleTesSetUptWithDaggerMock() = runBlockingTest {
        setUpCurrencyRateService("mock/latest_rates_euro.json")
        activityRule.launchActivity(null)
    }

    private suspend fun setUpCurrencyRateService(path: String) {
        val response = responseUtils.loadJson(
            path,
            CurrencyRateListResponse::class.java
        )
        whenever(service.fetchCurrencyRatesList(any())).doReturn(
            response
        )
    }

    private suspend fun setUpCurrencyRateServiceWithNetworkError(path: String) {
        val response = responseUtils.loadJson(
            path,
            CurrencyRateListResponse::class.java
        )
        whenever(service.fetchCurrencyRatesList(any())).doReturn(
            response
        )
    }
}