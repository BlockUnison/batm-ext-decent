package com.generalbytes.batm.server.extensions.extra.decent.factories

import cats.implicits._
import com.generalbytes.batm.common.adapters.RateSourceAdapter
import com.generalbytes.batm.common.domain.{Attempt, Currency, Task}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.factories.RateSourceFactory
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced
import com.generalbytes.batm.server.extensions.extra.decent.sources.bittrex.BittrexWrapperRateSource

trait BittrexRateSourceFactory extends RateSourceFactory {
  private val rateSourceLoginData = """bittrex:([A-Z,]+)""".r

  private def parseLoginInfo(loginInfo: String): Option[List[Currency]] = loginInfo match {
    case rateSourceLoginData(intermediates) => Currency.parseCSV(intermediates).some
    case _ => none
  }

  def createRateSource(loginInfo: String): Attempt[IRateSourceAdvanced] =
    parseLoginInfo(loginInfo) map { intermediates =>
      new RateSourceAdapter[Task](
        new BittrexWrapperRateSource(intermediates)
      )
    } toRight err"Could not create exchange from the parameters: $loginInfo"
}
