package com.generalbytes.batm.server.extensions.extra.decent.wallets.dctd

import cats.Applicative
import com.generalbytes.batm.common.Alias.Interpreter
import com.generalbytes.batm.common.Wallet
import com.generalbytes.batm.common.adapters.WalletAdapter

class DecentWalletAdapter[F[_] : Interpreter : Applicative](client: Wallet[F]) extends WalletAdapter[F](client)
