package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.friszing.rates.module.currencycalculator.model.CurrencyCalculatorItem
import com.friszing.rates.module.currencycalculator.model.CurrencyDetail
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorChangeBaseCalculationValueUseCaseImplTest {
    @Mock
    private lateinit var configuration: CurrencyCalculatorConfiguration

    @InjectMocks
    private lateinit var changeBaseCalculationValueUseCase: CurrencyCalculatorChangeBaseCalculationValueUseCaseImpl

    @Test
    fun `Should change the base currency when the base currency is changed`() {
        // WHEN
        changeBaseCalculationValueUseCase.invoke(
            CurrencyCalculatorItem(
                CurrencyDetail(
                    "USD",
                    "",
                    ""
                ),
                10.0
            )
        )

        // THEN
        verify(configuration).baseCurrency = "USD"
    }

    @Test
    fun `Should change the calculation value when the base currency value is changed`() {
        // WHEN
        changeBaseCalculationValueUseCase.invoke(
            CurrencyCalculatorItem(
                CurrencyDetail(
                    "USD",
                    "",
                    ""
                ),
                10.0
            )
        )

        // THEN
        verify(configuration).baseCalculationValue = 10.0
    }
}