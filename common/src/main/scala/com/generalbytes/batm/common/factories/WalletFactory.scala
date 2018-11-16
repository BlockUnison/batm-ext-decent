package com.generalbytes.batm.common.factories

import com.generalbytes.batm.common.domain.Attempt
import com.generalbytes.batm.common.domain.Wallet

trait WalletFactory[F[_]] {
  def createWallet(loginInfo: String): Attempt[Wallet[F]]
}
