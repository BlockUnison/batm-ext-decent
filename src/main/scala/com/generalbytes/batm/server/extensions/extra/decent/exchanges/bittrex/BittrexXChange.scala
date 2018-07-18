package com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex

import java.util

import com.generalbytes.batm.common.Alias.Address
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common._
import com.generalbytes.batm.server.extensions.extra.common.XChangeExchange
import org.knowm.xchange.ExchangeSpecification

class BittrexXChange(exchangeSpec: ExchangeSpecification, fiatCurrency: FiatCurrency) extends XChangeExchange(exchangeSpec, fiatCurrency.name) {
  override protected def isWithdrawSuccessful(result: Address): Boolean = true

  override protected def getAllowedCallsPerSecond: Double = 10.0

  override def getCryptoCurrencies: util.Set[String] = Currency.cryptos.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = Currency.fiats.map(_.name).toJavaSet   // .filter(_ != Currency.Euro)
}

object BittrexXChange {
  def defaultExchangeSpec: ExchangeSpecification = new org.knowm.xchange.bittrex.BittrexExchange().getDefaultExchangeSpecification
}