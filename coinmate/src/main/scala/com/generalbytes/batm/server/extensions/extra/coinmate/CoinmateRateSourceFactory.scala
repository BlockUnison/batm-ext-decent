package com.generalbytes.batm.server.extensions.extra.coinmate

import com.generalbytes.batm.common.adapters.RateSourceAdapter
import com.generalbytes.batm.common.domain.{Attempt, Task}
import com.generalbytes.batm.common.factories.RateSourceFactory
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced

trait CoinmateRateSourceFactory extends RateSourceFactory with CoinmateCredentialsFactory {
  def createRateSource(loginInfo: String): Attempt[IRateSourceAdvanced] =
    parseLoginInfo(loginInfo).map { creds =>
      new RateSourceAdapter(
        new CoinmateXchangeWrapper[Task](creds)
      )
    }.toRight(err"Could not create rate source from params: $loginInfo")
}
