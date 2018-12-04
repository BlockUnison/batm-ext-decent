package com.generalbytes.batm.server.extensions.extra.bitcoincash.exchanges.bch_coinmate

import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.coinmate.{CoinmateLoginInfo, CoinmateXchangeWrapper}

class CoinmateXChangeExchange(credentials: CoinmateLoginInfo)
  extends ExchangeAdapter(new CoinmateXchangeWrapper[Task](credentials))