package com.generalbytes.batm.common

import Alias.{Address, Identifier, Task}

trait Exchange {
  val cryptoCurrencies: Set[CryptoCurrency]
  val fiatCurrencies: Set[FiatCurrency]
  val preferredFiat: FiatCurrency

  def getBalance[T <: Currency]: BigDecimal
  def getAddress[T <: Currency]: Address
  def fulfillOrder[T <: Currency](order: Order[T]): Task[Identifier]
}
