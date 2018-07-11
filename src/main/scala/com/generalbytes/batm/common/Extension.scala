package com.generalbytes.batm.common

import Alias.{Attempt, Task}

trait Extension[T <: Currency] {
  val name: String
  val supportedCryptoCurrencies: Set[CryptoCurrency]
  def createWallet(loginInfo: String): Attempt[Wallet[T, Task]]
}
