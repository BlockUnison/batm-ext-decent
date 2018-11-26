package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.server.extensions.extra.decent.factories.BittrexExchangeFactory
import org.scalatest.{FlatSpec, Matchers}

class SubstitutingBittrexXchangeTest extends FlatSpec with Matchers {

//  val zero: BigDecimal = BigDecimal.valueOf(0.0)
//  private val credentials = Credentials("9c1b049844d84271b7a606311953b758", "1607470db4dc4fddb56eb58df156f672")
//  private val underlying = new BittrexXChangeWrapper[Task](credentials)
//  private val exchange = new OrderChainingBittrexXChangeWrapper(underlying.withRetries(2), Currency.Bitcoin)

  it should "create instance based on loginInfo string" in {
    val loginInfo = "bittrex:9c1b049844d84271b7a606311953b758:1607470db4dc4fddb56eb58df156f672:EUR->BTC,USD->BTC:USD"
    val instance = new BittrexExchangeFactory{}.createExchange(loginInfo)

    instance.isLeft shouldEqual false
    instance.getOrElse(null) should not be null
  }
}
