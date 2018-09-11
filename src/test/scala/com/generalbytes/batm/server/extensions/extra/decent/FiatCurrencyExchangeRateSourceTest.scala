package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.Currency
import com.generalbytes.batm.server.extensions.extra.decent.sources.btrx.FiatCurrencyExchangeRateSource
import org.scalatest.{FlatSpec, Matchers}

class FiatCurrencyExchangeRateSourceTest extends FlatSpec with Matchers {
  it should "get exchange rate" in {
    val subject = new FiatCurrencyExchangeRateSource[Task](Currency.Euro, Currency.USDollar)

    subject.currentRate.attempt.unsafeRunSync().right.get should be > BigDecimal.valueOf(0L)
  }
}
