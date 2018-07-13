package com.generalbytes.batm.common

import java.util
import Util._
import com.generalbytes.batm.server.extensions.IRateSource

class RateSourceAdapter(rs: RateSource) extends IRateSource {
  override def getCryptoCurrencies: util.Set[String] = rs.cryptoCurrencies.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = rs.fiatCurrencies.map(_.name).toJavaSet

  override def getExchangeRateLast(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal = {
    // TODO: Solve Value/Type problem
    val currencyPair = CurrencyPair(Currency.Decent, Currency.Euro)
    rs.getExchangeRate(currencyPair)
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .getOrThrow
      .bigDecimal
  }

  override def getPreferredFiatCurrency: String = rs.preferredFiat.name
}
