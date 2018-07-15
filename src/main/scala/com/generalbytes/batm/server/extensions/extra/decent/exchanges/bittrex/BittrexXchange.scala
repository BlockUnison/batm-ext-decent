package com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex

import java.util

import com.generalbytes.batm.common.Util._
import org.knowm.xchange.ExchangeSpecification
import com.generalbytes.batm.common.Alias.{Address, Identifier, Task}
import com.generalbytes.batm.common._
import com.generalbytes.batm.server.extensions.extra.common.XChangeExchange

//class BittrexExchange extends Exchange {
//  override def getBalance[T <: Currency]: Task[BigDecimal] = ???
//
//  override def getAddress[T <: Currency]: Task[Address] = ???
//
//  override def fulfillOrder[T <: Currency](order: Order[T]): Task[Identifier] = ???
//
//  override val cryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent, Currency.Bitcoin)
//  override val fiatCurrencies: Set[FiatCurrency] = Set(Currency.Euro)
//  override val preferredFiat: FiatCurrency = Currency.Euro
//}


class BittrexXchange(fiatCurrency: FiatCurrency, exchangeSpec: ExchangeSpecification = BittrexXchange.defaultExchangeSpec) extends XChangeExchange(exchangeSpec, fiatCurrency.name) {
  override protected def isWithdrawSuccessful(result: Address): Boolean = true

  override protected def getAllowedCallsPerSecond: Double = 10

  override def getCryptoCurrencies: util.Set[String] = Currency.cryptos.map(_.name).toJavaSet

  override def getFiatCurrencies: util.Set[String] = Currency.fiats.map(_.name).toJavaSet
}

object BittrexXchange {
  def defaultExchangeSpec: ExchangeSpecification = new org.knowm.xchange.bittrex.BittrexExchange().getDefaultExchangeSpecification
}