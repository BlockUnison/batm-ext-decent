package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex.{BittrexXChangeWrapper, OrderChainingBittrexXChangeWrapper}
import com.generalbytes.batm.server.extensions.extra.decent.factories.{BittrexExchangeFactory, Credentials}
import org.scalatest.{FlatSpec, Matchers}

class SubstitutingBittrexXchangeTest extends FlatSpec with Matchers with TestLoggingSupport {

  val zero: BigDecimal = BigDecimal.valueOf(0.0)
  private val credentials = Credentials("9c1b049844d84271b7a606311953b758", "1607470db4dc4fddb56eb58df156f672")
  private val underlying = new BittrexXChangeWrapper[Task](credentials)
  private val exchange = new OrderChainingBittrexXChangeWrapper(underlying.withRetries(2), Currency.Bitcoin)

  it should "create instance based on loginInfo string" in {
    val loginInfo = "bittrex:9c1b049844d84271b7a606311953b758:1607470db4dc4fddb56eb58df156f672:EUR->BTC,USD->BTC:USD"
    val instance = new BittrexExchangeFactory{}.createExchange(loginInfo)

    instance.isLeft shouldEqual false
    instance.getOrElse(null) should not be null
  }

  it should "get balance in replace currency" in {
    underlying.getBalance(Currency.Bitcoin).unsafeRunSync() shouldEqual exchange.getBalance(Currency.USDollar).unsafeRunSync()
  }

//  it should "not fail when processing BUY order USD->DCT" in {
//    val exchange = createExchange
//
//    val amount = BigDecimal(200)
//    val order = TradeOrder.buy(Currency.Decent, Currency.USDollar, amount)
//    val result = exchange.fulfillOrder(order).attempt.unsafeRunSync()
//    result.left.foreach(println)
//    result.foreach(println)
//    result.getOrThrow should not be empty
//  }

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
