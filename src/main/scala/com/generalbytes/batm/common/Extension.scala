package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.{Attempt, Task}
import com.generalbytes.batm.server.extensions.{IExchangeAdvanced, IRateSourceAdvanced}

trait Extension[T <: Currency] {
  val name: String
  val supportedCryptoCurrencies: Set[CryptoCurrency]
  def createWallet(loginInfo: String): Attempt[Wallet[T, Task]]
  def createRateSource(loginInfo: String): Attempt[IRateSourceAdvanced]    // TODO: Replace w/ RateSource
  def createExchange(loginInfo: String): Attempt[IExchangeAdvanced]   // TODO: Replace w/ Exchange
}
