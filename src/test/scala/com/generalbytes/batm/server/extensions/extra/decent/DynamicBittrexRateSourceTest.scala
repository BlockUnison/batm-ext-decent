package com.generalbytes.batm.server.extensions.extra.decent

import cats.implicits._
import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.{Currency, CurrencyPair, LoggingSupport}
import com.generalbytes.batm.server.extensions.extra.decent.sources.btrx.BittrexWrapperRateSource
import org.scalatest.{FlatSpec, Matchers}

class DynamicBittrexRateSourceTest extends FlatSpec with Matchers with LoggingSupport {
  val zero: BigDecimal = BigDecimal.valueOf(0L)

  it should "get the buy rate source" in {
    val rs = new BittrexWrapperRateSource[Task](Currency.USDollar :: Currency.Bitcoin :: Nil)
    val result = rs.getExchangeRateForBuy(CurrencyPair(Currency.Euro, Currency.Decent)).unsafeRunSync()
    result should be > zero
  }
}
