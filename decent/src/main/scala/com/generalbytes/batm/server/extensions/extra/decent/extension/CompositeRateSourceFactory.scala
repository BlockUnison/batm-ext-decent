package com.generalbytes.batm.server.extensions.extra.decent.extension

import cats.implicits._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.adapters.RateSourceAdapter
import com.generalbytes.batm.common.factories.RateSourceFactory
import com.generalbytes.batm.common.utils.LoggingSupport
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced
import com.generalbytes.batm.server.extensions.extra.decent.sources.btrx.BittrexWrapperRateSource

trait CompositeRateSourceFactory extends RateSourceFactory with FixedPriceRateSourceFactory with LoggingSupport {

  def create(loginInfo: String): Attempt[IRateSourceAdvanced] =
      new RateSourceAdapter[Task](
        new BittrexWrapperRateSource(List(Currency.USDollar, Currency.Bitcoin))
      ).asRight

  override def createRateSource(loginInfo: String): Attempt[IRateSourceAdvanced] =
    super[FixedPriceRateSourceFactory].createRateSource(loginInfo) orElse create(loginInfo)
}
