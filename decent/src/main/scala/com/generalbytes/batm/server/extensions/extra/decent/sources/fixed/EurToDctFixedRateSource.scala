package com.generalbytes.batm.server.extensions.extra.decent.sources.fixed

import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.ratesources.SingleFixedPriceRateSource

class EurToDctFixedRateSource[F[_] : ErrorApplicative](rate: BigDecimal)
  extends SingleFixedPriceRateSource[F](CurrencyPairF2C(Currency.Euro, Currency.Decent), rate)

object EurToDctFixedRateSource {
  val defaultRate = BigDecimal(5.1)
}
