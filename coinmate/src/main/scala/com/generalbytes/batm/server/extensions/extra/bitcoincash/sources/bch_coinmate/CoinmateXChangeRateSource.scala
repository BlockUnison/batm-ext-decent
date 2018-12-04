package com.generalbytes.batm.server.extensions.extra.bitcoincash.sources.bch_coinmate

import com.generalbytes.batm.common.adapters.RateSourceAdapter
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.server.extensions.extra.coinmate.{CoinmateLoginInfo, CoinmateXchangeWrapper}

class CoinmateXChangeRateSource(credentials: CoinmateLoginInfo)
  extends RateSourceAdapter(new CoinmateXchangeWrapper[Task](credentials))
