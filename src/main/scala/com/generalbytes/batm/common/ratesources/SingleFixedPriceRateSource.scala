package com.generalbytes.batm.common.ratesources

import cats.effect.IO
import cats.implicits._
import com.generalbytes.batm.common.Alias.{ExchangeRate, Task}
import com.generalbytes.batm.common.{CryptoCurrency, CurrencyPair, FiatCurrency, RateSource}

class SingleFixedPriceRateSource[F <: CryptoCurrency, T <: FiatCurrency](currencyPair: CurrencyPair[F, T], rate: ExchangeRate) extends RateSource {
  override def getExchangeRate[A <: CryptoCurrency, B <: FiatCurrency](currencyPair: CurrencyPair[A, B]): Task[ExchangeRate] =
    if (currencyPair == this.currencyPair) {    // TODO: triple equals using Eq[_]
      IO.pure(rate.asRight)
    } else IO.pure("Unsupported currency".asLeft)

  override val cryptoCurrencies: Set[CryptoCurrency] = Set(currencyPair.from)
  override val fiatCurrencies: Set[FiatCurrency] = Set(currencyPair.to)
  override val preferredFiat: FiatCurrency = currencyPair.to
}
