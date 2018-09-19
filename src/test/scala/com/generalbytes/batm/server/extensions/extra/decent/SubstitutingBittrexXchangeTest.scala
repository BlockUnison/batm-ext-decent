package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.{Currency, LoggingSupport, TradeOrder}
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.{DefaultBittrexXChangeWrapper, SubstitutingBittrexXChangeWrapper}
import com.generalbytes.batm.server.extensions.extra.decent.extension.LoginInfo
import com.generalbytes.batm.common.implicits._
import org.scalactic.source.Position
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec, Matchers}

class SubstitutingBittrexXchangeTest extends FlatSpec with Matchers with LoggingSupport with BeforeAndAfter {

  val zero: BigDecimal = BigDecimal.valueOf(0.0)

  before {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG")
    val f = logger.getClass.getDeclaredField("currentLogLevel")
    f.setAccessible(true)
    f.get(logger) |> println
  }

  protected def createExchange: SubstitutingBittrexXChangeWrapper[Task] = {
    val credentials = LoginInfo("9c1b049844d84271b7a606311953b758", "1607470db4dc4fddb56eb58df156f672")
    val exchange = new SubstitutingBittrexXChangeWrapper(new DefaultBittrexXChangeWrapper[Task](credentials).withRetries(2), Currency.Bitcoin)
    exchange
  }

  it should "not fail when processing BUY order USD->DCT" in {
    val exchange = createExchange

    val amount = BigDecimal(80)
    val order = TradeOrder.buy(Currency.Decent, Currency.USDollar, amount)
    val result = exchange.fulfillOrder(order).attempt.unsafeRunSync()
    result.left.foreach(println)
    result.foreach(println)
    result.getOrThrow should not be empty
  }

//  it should "not fail when processing sell order (BTC -> USD)" in {
//    val credentials = LoginInfo("ecfd6e9a0a45480e8d695ae70912319f", "2367ac62c29440f5a758b90a7ec1e0e4")
//    val exchange = new DefaultBittrexXChangeWrapper(credentials)
//
//    val amount = BigDecimal(11)
//    val order = TradeOrder.sell(Currency.Bitcoin, Currency.USDollar, amount)
//    val result = exchange.fulfillOrder(order)
//    val value = result.attempt.unsafeRunSync().log.getOrThrow
//    println(value)
//    value should not be empty
//  }
}
