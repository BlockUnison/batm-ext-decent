package com.generalbytes.batm.server.extensions.extra.decent

import org.scalatest.{FlatSpec, Matchers}

class WalletTest extends FlatSpec with Matchers {
  it should "create wallet from valid string" in {
    val ext = new AdapterDecentExtension()
    val loginInfo = "dctd:http:admin:admin:richard"

    val result = ext.createWallet(loginInfo)
    result should not be null
  }

  it should "return null when creating wallet from invalid string" in {
    val ext = new AdapterDecentExtension()
    val loginInfo = "dc:a:a:a:aa"

    val result = ext.createWallet(loginInfo)
    result should be(null)
  }
}
