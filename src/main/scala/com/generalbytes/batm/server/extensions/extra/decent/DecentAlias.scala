package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.{Currency, Wallet}

object DecentAlias {
  type WalletApi = Wallet[Currency.DCT, Task]
}
