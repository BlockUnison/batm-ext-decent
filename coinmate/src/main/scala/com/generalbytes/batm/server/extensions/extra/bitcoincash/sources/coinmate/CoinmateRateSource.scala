package com.generalbytes.batm.server.extensions.extra.bitcoincash.sources.coinmate

import com.generalbytes.batm.common.adapters.RateSourceAdapter
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.server.extensions.extra.coinmate.{CoinmateLoginInfo, CoinmateXchangeWrapper}

class CoinmateRateSource(credentials: CoinmateLoginInfo)
  extends RateSourceAdapter(new CoinmateXchangeWrapper[Task](credentials))
