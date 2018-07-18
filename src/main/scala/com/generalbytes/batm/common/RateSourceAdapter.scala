package com.generalbytes.batm.common

import java.util

import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced

class RateSourceAdapter(rs: RateSource) extends IRateSourceAdvanced {
  override def getCryptoCurrencies: util.Set[String] = rs.cryptoCurrencies.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = rs.fiatCurrencies.map(_.name).toJavaSet

  override def getExchangeRateLast(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForSell(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .bigDecimal

  override def getPreferredFiatCurrency: String = rs.preferredFiat.name

  private def request[A](task: Task[A]): A =
    task.unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .getOrThrow

  override def getExchangeRateForBuy(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForBuy(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .bigDecimal

  override def getExchangeRateForSell(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForBuy(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .bigDecimal

  override def calculateBuyPrice(cryptoCurrency: String, fiatCurrency: String, amount: java.math.BigDecimal): java.math.BigDecimal =
    getExchangeRateForBuy(cryptoCurrency, fiatCurrency).multiply(amount)

  override def calculateSellPrice(cryptoCurrency: String, fiatCurrency: String, amount: java.math.BigDecimal): java.math.BigDecimal =
    getExchangeRateForSell(cryptoCurrency, fiatCurrency).multiply(amount)
}
