package com.generalbytes.batm.server.extensions.extra.decent

import common.Alias.Task
import common.{Currency, Wallet}

object Decent {
  type WalletApi = Wallet[Currency.DCT, Task]
}
