package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.common.Currency
import org.scalatest.{FlatSpec, Matchers}

class WalletTest extends FlatSpec with Matchers {
  private val validLogin = "dctd:http:admin:admin:decentatm.hypersignal.xyz"
  it should "create wallet from valid string" in {
    val ext = new AdapterDecentExtension()
    val loginInfo = validLogin

    val result = ext.createWallet(loginInfo)
    result should not be null
  }

  it should "return null when creating wallet from invalid string" in {
    val ext = new AdapterDecentExtension()
    val loginInfo = "dc:a:a:a:aa"

    val result = ext.createWallet(loginInfo)
    result should be(null)
  }

  it should "call service when sending money from wallet" in {
    val ext = new AdapterDecentExtension()
    val loginInfo = validLogin

    val wallet = ext.createWallet(loginInfo)
    val targetAddress = "asdfh376"
    val amount = BigDecimal.valueOf(100)
    val txId = wallet.sendCoins(targetAddress, amount.bigDecimal, Currency.Decent.name, "")

    txId should not be null
  }
}
