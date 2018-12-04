package com.generalbytes.batm.server.extensions.extra.coinmate

import com.generalbytes.batm.common.domain.{Currency, Task, TradeOrder}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.utils.LoggingSupport
import org.scalatest.{FlatSpec, Matchers}

class CoinmateExchangeTest extends FlatSpec with Matchers with LoggingSupport {
  val clientId = "34813"
  val pubKey = "k0w_LMZzhZVLdsvww5ej5PQCoZmxDNRY1mFOvRFMQNM"
  val privKey = "ufjJ1vmCQBrp5HxymX76XbvuMCsVIm0n_MZcmM0qm1M"
  val creds = CoinmateLoginInfo(clientId, pubKey, privKey)
  val zero = BigDecimal(0)

  it should "get balance EUR" in {
    val exchange = new CoinmateXchangeWrapper[Task](creds)

    val balance = exchange.getBalance(Currency.Euro).unsafeRunSync()
    println(balance)

    balance should be > zero
  }

  it should "get balance BCH" in {
    val exchange = new CoinmateXchangeWrapper[Task](creds)

    val balance = exchange.getBalance(Currency.BitcoinCash).unsafeRunSync()
    println(balance)

    balance should be > zero
  }

  it should "buy BCH for EUR" in {
    val exchange = new CoinmateXchangeWrapper[Task](creds)

    val txId = exchange.fulfillOrder(TradeOrder.buy(Currency.BitcoinCash, Currency.Euro, 10.0)).unsafeRunSync()
    println(txId)

    txId should not be empty
  }

  it should "sell BCH for EUR" in {
    val exchange = new CoinmateXchangeWrapper[Task](creds)

    val txId = exchange.fulfillOrder(TradeOrder.sell(Currency.BitcoinCash, Currency.Euro, 0.02)).unsafeRunSync()
    println(txId)

    txId should not be empty
  }
}
