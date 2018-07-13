package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.Task

// TODO: RateSource as typeclass?
trait RateSource extends ExchangeBase{
  def getExchangeRate[F <: CryptoCurrency, T <: FiatCurrency](currencyPair: CurrencyPair[F, T]): Task[BigDecimal]
}

trait ExchangeBase {
  val cryptoCurrencies: Set[CryptoCurrency]
  val fiatCurrencies: Set[FiatCurrency]
  val preferredFiat: FiatCurrency
}
