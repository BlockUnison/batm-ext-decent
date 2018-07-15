package com.generalbytes.batm.common

import Alias.{Attempt, Task}
import com.generalbytes.batm.server.extensions.{IExchange, IExchangeAdvanced, IRateSourceAdvanced}

trait Extension[T <: Currency] {
  val name: String
  val supportedCryptoCurrencies: Set[CryptoCurrency]
  def createWallet(loginInfo: String): Attempt[Wallet[T, Task]]
  def createRateSource: Attempt[IRateSourceAdvanced]
  def createExchange(loginInfo: String): Attempt[IExchangeAdvanced]
}
