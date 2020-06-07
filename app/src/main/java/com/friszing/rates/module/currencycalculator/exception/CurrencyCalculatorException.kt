package com.friszing.rates.module.currencycalculator.exception

sealed class CurrencyCalculatorException(
    override val message: String?,
    override val cause: Throwable?
) : Throwable(message, cause) {
    class CurrencyCalculatorGeneralException(
        override val cause: Throwable?
    ) : CurrencyCalculatorException(null, cause)

    class CurrencyCalculatorParseException(
        override val cause: Throwable?
    ) : CurrencyCalculatorException(null, cause)

    class CurrencyCalculatorConnectionErrorException(
        override val cause: Throwable?
    ) : CurrencyCalculatorException(null, cause)
}