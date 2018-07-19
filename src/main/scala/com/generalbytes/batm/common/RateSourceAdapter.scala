package com.generalbytes.batm.common

import java.util

import com.generalbytes.batm.common.Alias.{Attempt, Task}
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced
import com.typesafe.scalalogging.Logger

class RateSourceAdapter(rs: RateSource) extends IRateSourceAdvanced {
  implicit val logger: Logger = Logger[this.type]

  override def getCryptoCurrencies: util.Set[String] = rs.cryptoCurrencies.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = rs.fiatCurrencies.map(_.name).toJavaSet

  override def getExchangeRateLast(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForSell(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .map(_.bigDecimal).getOrNull

  override def getPreferredFiatCurrency: String = rs.preferredFiat.name

  private def request[A](task: Task[A]): Attempt[A] =
    task.unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .logError

  override def getExchangeRateForBuy(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForBuy(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .map(_.bigDecimal).getOrNull

  override def getExchangeRateForSell(cryptoCurrency: String, fiatCurrency: String): java.math.BigDecimal =
    request(rs.getExchangeRateForBuy(CurrencyPair.fromNames(cryptoCurrency, fiatCurrency).getOrThrow))
      .map(_.bigDecimal).getOrNull

  override def calculateBuyPrice(cryptoCurrency: String, fiatCurrency: String, amount: java.math.BigDecimal): java.math.BigDecimal =
    Option(getExchangeRateForBuy(cryptoCurrency, fiatCurrency)).map(_.multiply(amount)).orNull

  override def calculateSellPrice(cryptoCurrency: String, fiatCurrency: String, amount: java.math.BigDecimal): java.math.BigDecimal =
    Option(getExchangeRateForSell(cryptoCurrency, fiatCurrency)).map(_.divide(amount)).orNull
}
