package com.generalbytes.batm.server.extensions.extra.decent

import cats.implicits._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.wallets.dctd.DecentWalletRestApi
import org.http4s.Uri
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.{Logger, LoggerFactory}

class DecentWalletRestApiTest extends FlatSpec with Matchers {
  implicit val logger: Logger = LoggerFactory.getLogger("test")
  val zero: BigDecimal = BigDecimal.valueOf(0L)

  it should "return a nice error when sending money to invalid address" in {
    val uri = Uri.unsafeFromString("http://207.154.255.239:9696")
    val creds = DecentWalletRestApi.DecentWalletCredentials("admin", "admin")

    val walletApi = new DecentWalletRestApi(uri, creds)
    val error = walletApi.issuePayment("N/A", 10).attempt.unsafeRunSync().left.getOrElse(null)
    error should not be null
  }

  it should "return work when sending a small payment" in {
    val uri = Uri.unsafeFromString("http://207.154.255.239:9696")
    val creds = DecentWalletRestApi.DecentWalletCredentials("admin", "admin")

    val walletApi = new DecentWalletRestApi(uri, creds)
    val validAddress = "ud4842222bc4bba988f3f696929dab106"
    val result = walletApi.issuePayment(validAddress, 0.01).attempt.unsafeRunSync().log
    result.left.foreach(println)
    result.right.get should not be empty
  }

  it should "get balance" in {
    val uri = Uri.unsafeFromString("http://207.154.255.239:9696")
    val creds = DecentWalletRestApi.DecentWalletCredentials("admin", "admin")

    val walletApi = new DecentWalletRestApi(uri, creds)
    val result = walletApi.getBalance.attempt.unsafeRunSync().log
    result.left.getOrElse(null) should be (null)
    result.right.get should be > zero
  }
}
