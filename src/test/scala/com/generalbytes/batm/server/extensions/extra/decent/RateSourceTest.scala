package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.server.extensions.IRateSourceAdvanced
import org.scalatest.{FlatSpec, Matchers}

class RateSourceTest extends FlatSpec with Matchers {
  it should "create rate source w/o problems" in {
    val ext = new AdapterDecentExtension()
    val sourceLogin = null
    val rs = ext.createRateSource(sourceLogin)

    rs should not be null
  }

  it should "get exchange rate last" in {
    val ext = new AdapterDecentExtension()
    val sourceLogin = "fixed:100.0"
    val rs = ext.createRateSource(sourceLogin)

    val rate = rs.getExchangeRateLast("DCT", "EUR") |> BigDecimal.apply
    rate should be > BigDecimal(0.0)
  }

  it should "get exchange rate sell" in {
    val ext = new AdapterDecentExtension()
    val sourceLogin = "fixed:100.0"
    val rs = ext.createRateSource(sourceLogin).asInstanceOf[IRateSourceAdvanced]

    val rate = rs.calculateBuyPrice("DCT", "EUR", BigDecimal(1.0).bigDecimal) |> BigDecimal.apply
    rate should be > BigDecimal(0.0)
  }
}
