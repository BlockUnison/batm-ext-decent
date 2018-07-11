package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.server.common.Alias.Task
import com.generalbytes.batm.server.common.{Currency, Wallet}
import com.generalbytes.batm.server.common.Wallet

object Decent {
  type WalletApi = Wallet[Currency.DCT, Task]
}
