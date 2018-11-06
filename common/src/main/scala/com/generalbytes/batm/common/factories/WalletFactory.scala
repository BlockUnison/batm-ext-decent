package com.generalbytes.batm.common.factories

import com.generalbytes.batm.common.Alias.Attempt
import com.generalbytes.batm.common.{Currency, Wallet}

trait WalletFactory[F[_]] {
  def createWallet(loginInfo: String): Attempt[Wallet[F]]
}
