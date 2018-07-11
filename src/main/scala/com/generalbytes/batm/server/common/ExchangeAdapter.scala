package com.generalbytes.batm.server.common

import java.util
import java.math._

import com.generalbytes.batm.server.extensions.IExchange
import com.typesafe.scalalogging.Logger
import Util._

class ExchangeAdapter(xch: Exchange) extends IExchange {
  implicit val logger: Logger = Logger[ExchangeAdapter]

  override def getCryptoCurrencies: util.Set[String] = xch.cryptoCurrencies.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = xch.fiatCurrencies.map(_.name).toJavaSet

  override def getPreferredFiatCurrency: String = xch.preferredFiat.name

  override def getCryptoBalance(s: String): java.math.BigDecimal = ???

  override def getFiatBalance(s: String): java.math.BigDecimal = ???

  override def purchaseCoins(amount: BigDecimal, cryptoCurrency: String, fiatCurrency: String, description: String): String = {
//    val crypto = Currency.withName[CryptoCurrency](cryptoCurrency).getOrThrow
//    val fiat = Currency.withName[FiatCurrency](fiatCurrency).getOrThrow

    val order = new Order[Currency] {}    // TODO create from currencypair and amount
    xch.fulfillOrder(order)
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .logError
      .value
  }

  override def sellCoins(bigDecimal: java.math.BigDecimal, s: String, s1: String, s2: String): String = ???

  override def sendCoins(s: String, bigDecimal: java.math.BigDecimal, s1: String, s2: String): String = ???

  override def getDepositAddress(s: String): String = ???
}
