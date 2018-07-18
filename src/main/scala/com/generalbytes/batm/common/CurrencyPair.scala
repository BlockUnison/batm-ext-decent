package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.Attempt
import Util._

case class CurrencyPair(from: CryptoCurrency, to: FiatCurrency)

object CurrencyPair {
  def fromNames(from: String, to: String): Attempt[CurrencyPair] = for {
    f <- Currency.withName(from).cast[CryptoCurrency]
    t <- Currency.withName(to).cast[FiatCurrency]
  } yield CurrencyPair(f, t)
}