package com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex

import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.domain.Task
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.factories.Credentials

class BittrexExchange(credentials: Credentials) extends ExchangeAdapter(new BittrexXChangeWrapper[Task](credentials))
