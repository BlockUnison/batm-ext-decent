package com.generalbytes.batm.server.extensions.extra.bitcoincash.exchanges.bch_coinmate

import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.bch_coinmate.{CoinmateLoginInfo, CoinmateXchangeWrapper}

class CoinmateExchange(credentials: CoinmateLoginInfo)
  extends ExchangeAdapter(new CoinmateXchangeWrapper[Task](credentials))