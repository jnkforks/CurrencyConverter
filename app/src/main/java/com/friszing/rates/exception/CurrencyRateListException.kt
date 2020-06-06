package com.friszing.rates.exception

sealed class CurrencyRateListException(
    override val message: String?,
    override val cause: Throwable?
) : Throwable(message, cause) {
    class CurrencyRateListGeneralException(
        override val cause: Throwable?
    ) : CurrencyRateListException(null, cause)

    class CurrencyRateListParseException(
        override val cause: Throwable?
    ) : CurrencyRateListException(null, cause)
}