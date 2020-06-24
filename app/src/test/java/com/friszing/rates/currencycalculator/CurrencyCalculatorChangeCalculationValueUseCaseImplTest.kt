package com.friszing.rates.currencycalculator

import com.friszing.rates.module.currencycalculator.configuration.CurrencyCalculatorConfiguration
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyCalculatorChangeCalculationValueUseCaseImplTest {

    @Mock
    private lateinit var configuration: CurrencyCalculatorConfiguration

    @InjectMocks
    private lateinit var changeCalculationValueUse: CurrencyCalculatorChangeCalculationValueUseCaseImpl

    @Test
    fun `Should change the calculation value`() {
        // WHEN
        changeCalculationValueUse.invoke(100.0)

        // THEN
        verify(configuration).baseCalculationValue = 100.0
    }
}