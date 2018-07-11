package com.generalbytes.batm.common

import Alias.{Attempt, Task}

trait WalletFactory[T <: Currency] {
  def create(loginInfo: String): Attempt[Wallet[T, Task]]
}
