package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.{Currency, CurrencyPair, TradeOrder}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.{CounterReplacingXChangeWrapper, DefaultBittrexXChangeWrapper, OrderChainingBittrexXChangeWrapper}
import com.generalbytes.batm.server.extensions.extra.decent.extension.LoginInfo
import org.scalatest.{FlatSpec, Matchers}

class CounterReplacementBittrexXChangeTest extends FlatSpec with Matchers with TestLoggingSupport {

  val zero: BigDecimal = BigDecimal.valueOf(0.0)
  private val credentials = LoginInfo("9c1b049844d84271b7a606311953b758", "1607470db4dc4fddb56eb58df156f672")
  private val underlying = new DefaultBittrexXChangeWrapper[Task](credentials)
  private val exchange = new CounterReplacingXChangeWrapper(underlying.withRetries(2), CurrencyPair(Currency.USDollar, Currency.Bitcoin) :: CurrencyPair(Currency.Euro, Currency.Bitcoin) :: Nil)

  it should "get balance in replace currency" in {
    underlying.getBalance(Currency.Bitcoin).unsafeRunSync() shouldEqual exchange.getBalance(Currency.USDollar).unsafeRunSync()
  }

  it should "get balance in other thanreplace currency" in {
    underlying.getBalance(Currency.Bitcoin).unsafeRunSync() should not equal exchange.getBalance(Currency.Decent).unsafeRunSync()
  }
  it should "not fail when processing BUY order USD->DCT" in {

    val amount = BigDecimal(90)
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