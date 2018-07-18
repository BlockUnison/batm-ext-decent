package com.generalbytes.batm.common.ratesources

import cats.effect.IO
import cats.implicits._
import com.generalbytes.batm.common.Alias.{ExchangeRate, Task}
import com.generalbytes.batm.common.{CryptoCurrency, CurrencyPair, FiatCurrency, RateSource}

class SingleFixedPriceRateSource[F <: CryptoCurrency, T <: FiatCurrency](currencyPair: CurrencyPair, rate: ExchangeRate) extends RateSource {
  override val cryptoCurrencies: Set[CryptoCurrency] = Set(currencyPair.from)
  override val fiatCurrencies: Set[FiatCurrency] = Set(currencyPair.to)
  override val preferredFiat: FiatCurrency = currencyPair.to

  override def getExchangeRateForSell(currencyPair: CurrencyPair): Task[ExchangeRate] =
    if (currencyPair == this.currencyPair) {
      IO.pure(rate.asRight)
    } else IO.pure("Unsupported currency".asLeft)

  override def getExchangeRateForBuy(currencyPair: CurrencyPair): Task[BigDecimal] =
    if (currencyPair == this.currencyPair) {
      IO.pure(rate.asRight)
    } else IO.pure("Unsupported currency".asLeft)
}
