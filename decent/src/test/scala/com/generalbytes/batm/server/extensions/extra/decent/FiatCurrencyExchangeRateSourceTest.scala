package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.sources.bittrex.FiatCurrencyExchangeTicker
import org.scalatest.{FlatSpec, Matchers}

class FiatCurrencyExchangeRateSourceTest extends FlatSpec with Matchers {
  it should "get exchange rate" in {
    val subject = new FiatCurrencyExchangeTicker[Task](CurrencyPair(Currency.Euro, Currency.USDollar))

    subject.currentRate.attempt.unsafeRunSync().right.get should be > BigDecimal.valueOf(0L)
  }
}
