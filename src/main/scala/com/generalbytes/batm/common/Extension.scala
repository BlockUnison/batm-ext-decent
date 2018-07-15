package com.generalbytes.batm.common

import Alias.{Attempt, Task}
import com.generalbytes.batm.server.extensions.IExchange

trait Extension[T <: Currency] {
  val name: String
  val supportedCryptoCurrencies: Set[CryptoCurrency]
  def createWallet(loginInfo: String): Attempt[Wallet[T, Task]]
  val rateSource: RateSource
  def createExchange(loginInfo: String): Attempt[IExchange]
}
