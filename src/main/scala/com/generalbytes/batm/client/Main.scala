package com.generalbytes.batm.client

import cats.effect._
import cats.implicits._
import com.generalbytes.batm.common.{CurrencyPair, Util}
import com.generalbytes.batm.server.extensions.extra.decent.DecentExtension
import com.generalbytes.batm.common.Alias.{Attempt, Identifier, Task}
import com.typesafe.scalalogging.Logger
import org.http4s.Uri

import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Try

object Main extends App {
  import Util._
  import com.generalbytes.batm.common.Currency._

  implicit val logger: Logger = Logger("Main")

  val host = "decentatm.hypersignal.xyz"

  def request(): Unit = {
    print("username (default 'admin'): ")
    val user = StdIn.readLine().some.filter(_.nonEmpty).getOrElse("admin")
    print("password (default 'admin')")
    val pass = StdIn.readLine().some.filter(_.nonEmpty).getOrElse("admin")
    print("amount (default '1000')")
    val amount = StdIn.readLine().some.flatMap(s => Try(s.toLong).toOption).getOrElse(1000L)
    print("address (default 'account')")
    val address = StdIn.readLine().some.filter(_.nonEmpty).getOrElse("account")

    // dctd:protocol:user:password:ip:port:accountname
    val loginStr = s"dctd:http:$user:$pass:$host"
    val result: Attempt[Task[Identifier]] = new DecentExtension().createWallet(loginStr).map(_.issuePayment(address, amount))
    result.flatSequence.asInstanceOf[IO[Attempt[Identifier]]].unsafeRunTimed(Util.defaultDuration).fold("Request timeout")(_.fold(identity, identity)) |> println
  }

  val apiHost = "https://10.0.2.15/apiv2/"

  val terminalSerialNumber = "VT123456"

  val apiKey = "PD8AEO3M8EMDTVS8D3YAHBDO72XAXX27H"

  val secretKey = "SV38ZIRABDDBTTP2E27B8SDUBUO8PUK3S"

  def runClient(): Unit = {
    println(s"Running as CLIENT\r\nURL is $host")
    val client = new VirtualApiClient(
      Uri.unsafeFromString(apiHost),
      terminalSerialNumber,
      apiKey,
      secretKey
    )

    val currencyPair = CurrencyPair(Decent, Euro)
    val resultIO = client.purchase(currencyPair, 1000L, "blahblah")
    val txId = resultIO.unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .logError
      .getOrNull

    println(s"txid: $txId")
  }

  def runHmac(): Unit = {
    println("Signature generator")
    print(s"Terminal serial number ($terminalSerialNumber): ")
    val serial = StdIn.readLine().some.filter(_.nonEmpty).getOrElse(terminalSerialNumber)
    print(s"Api key ($apiKey): ")
    val apiKeyActual = StdIn.readLine().some.filter(_.nonEmpty).map(_.trim).getOrElse(apiKey)
    print(s"Secret key ($secretKey): ")
    val secretKeyActual = StdIn.readLine().some.filter(_.nonEmpty).map(_.trim).getOrElse(secretKey)

    val nonce = System.currentTimeMillis

    val signature = Util.hmacsha256(nonce.toString + serial + apiKeyActual, secretKeyActual).toLowerCase
    println(s"Nonce: $nonce")
    println(s"Signature: $signature")
  }

  def run(): Unit = {
    println("Press 'c' for client, 'h' for hmac generation")
    val resp = StdIn.readLine().toLowerCase.trim
    if(resp == "c")
      runClient()
    else if(resp == "h") {
      runHmac()
    } else return
    run()
  }

  run()
}
