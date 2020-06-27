package com.friszing.rates

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.friszing.rates.currencycalculator.CurrencyCalculatorItemListMapperImpl
import com.friszing.rates.currencycalculator.CurrencyDetailProviderImpl
import com.friszing.rates.currencycalculator.CurrencyRateListResponseMapperImpl
import com.friszing.rates.currencycalculator.CurrencyValueCalculatorImpl
import com.friszing.rates.daggermock.createCurrencyCalculatorAppDaggerMockRule
import com.friszing.rates.di.module.StorageModule
import com.friszing.rates.module.currencycalculator.fragment.currencyCalculatorPage
import com.friszing.rates.module.currencycalculator.mapper.CurrencyCalculatorItemListMapper
import com.friszing.rates.module.currencycalculator.mapper.CurrencyRateListResponseMapper
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyRateListResponse
import com.friszing.rates.module.currencycalculator.service.CurrencyRateService
import com.friszing.rates.utils.ResponseUtils
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

@Ignore
class CurrencyCalculatorActivityTest {

    @get:Rule
    val activityRule =
        ActivityTestRule(CurrencyCalculatorActivity::class.java, true, false)

    @get:Rule
    val currencyCalculatorAppDaggerMockRule = createCurrencyCalculatorAppDaggerMockRule()

    @Mock
    private lateinit var service: CurrencyRateService

    private lateinit var responseMapper: CurrencyRateListResponseMapper

    private lateinit var currencyCalculatorItemListMapper: CurrencyCalculatorItemListMapper

    private val responseUtils = ResponseUtils()

    @Before
    fun setUp() {
        cleanSharedPreferences()
        setUpTestDependencies()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_get_latest_rates_successfully() = runBlockingTest {
        // GIVEN
        val expectedCurrencyRateList = getCurrencyRateList("mock/latest_rates_euro.json")
        setUpCurrencyRateService("EUR", "mock/latest_rates_euro.json")

        // WHEN
        activityRule.launchActivity(null)

        // THEN
        currencyCalculatorPage {
            checkAllCurrencyRateItems(expectedCurrencyRateList)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_show_error_when_the_fetching_latest_currencies_fails() = runBlockingTest {
        // GIVEN
        setUpCurrencyRateServiceWithNetworkError()

        // WHEN
        activityRule.launchActivity(null)

        // THEN
        currencyCalculatorPage {
            isSnackbarVisible()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_change_the_base_currency_when_the_user_clicks_other_currency() = runBlockingTest {
        // GIVEN
        val expectedCurrencyRateList =
            getCurrencyRateList("mock/latest_rates_usd.json", 113.7)
        setUpCurrencyRateService("EUR", "mock/latest_rates_euro.json")
        setUpCurrencyRateService("USD", "mock/latest_rates_usd.json")

        activityRule.launchActivity(null)
        currencyCalculatorPage {
            // WHEN
            selectCurrencyItem(1)
            Thread.sleep(2000L)

            // THEN
            checkAllCurrencyRateItems(expectedCurrencyRateList)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun should_change_the_currency_calculation_values_when_the_user_changes_the_base_currency_value() =
        runBlockingTest {
            // GIVEN
            val expectedCurrencyRateList =
                getCurrencyRateList("mock/latest_rates_euro.json", 200.0)
            setUpCurrencyRateService("EUR", "mock/latest_rates_euro.json")

            activityRule.launchActivity(null)
            currencyCalculatorPage {
                // WHEN
                changeCalculationValue(200.0)
                Thread.sleep(2000L)

                // THEN
                checkAllCurrencyRateItems(expectedCurrencyRateList)
            }
        }

    @ExperimentalCoroutinesApi
    @Test
    fun should_change_all_currency_values_to_zero_when_the_calculation_value_is_empty() =
        runBlockingTest {
            // GIVEN
            val expectedCurrencyRateList =
                getCurrencyRateList("mock/latest_rates_euro.json", 0.0)
            setUpCurrencyRateService("EUR", "mock/latest_rates_euro.json")

            activityRule.launchActivity(null)
            currencyCalculatorPage {
                // WHEN
                changeCalculationValueWithEmptyText()
                Thread.sleep(2000L)

                // THEN
                checkCalculatedCurrencyRateItems(expectedCurrencyRateList)
            }
        }

    @Test
    fun should_close_keyboard_when_the_base_value_input_lose_its_focus() = runBlockingTest {
        // GIVEN
        val expectedCurrencyRateList = getCurrencyRateList("mock/latest_rates_euro.json")
        setUpCurrencyRateService("EUR", "mock/latest_rates_euro.json")

        activityRule.launchActivity(null)
        currencyCalculatorPage {
            // GIVEN
            changeCalculationValue(10.0)

            // WHEN
            scrollPageTo(expectedCurrencyRateList.lastIndex)

            // THEN
            checkKeyboardVisibility(false)
        }
    }

    private suspend fun setUpCurrencyRateService(currency: String, path: String) {
        val currencyListResponse = responseUtils.loadJson(
            path,
            CurrencyRateListResponse::class.java
        )
        whenever(service.fetchCurrencyRatesList(currency)).doReturn(
            currencyListResponse
        )
    }

    private suspend fun setUpCurrencyRateServiceWithNetworkError() {
        whenever(service.fetchCurrencyRatesList("EUR")).doAnswer { throw IOException() }
    }

    private fun getCurrencyRateList(
        path: String,
        calculationValue: Double = 100.0
    ): List<CurrencyCalculatorItem> {
        val currencyListResponse = responseUtils.loadJson(
            path,
            CurrencyRateListResponse::class.java
        )
        val currencyRateList = responseMapper.map(currencyListResponse)
        return currencyCalculatorItemListMapper.map(currencyRateList, calculationValue)
    }

    private fun setUpTestDependencies() {
        // Unfortunately we cant use @InjectFromComponent if we use SubComponent of Dagger!
        responseMapper = CurrencyRateListResponseMapperImpl()
        currencyCalculatorItemListMapper =
            CurrencyCalculatorItemListMapperImpl(
                CurrencyDetailProviderImpl(),
                CurrencyValueCalculatorImpl()
            )
    }

    private fun cleanSharedPreferences() {
        InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext
            .deleteSharedPreferences(StorageModule.CONFIGURATION_PREF)
    }
}