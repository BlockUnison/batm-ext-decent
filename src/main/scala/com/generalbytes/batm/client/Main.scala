package com.generalbytes.batm.client

import cats.effect._
import cats.implicits._
import com.generalbytes.batm.common.{CurrencyPair, Util}
import com.generalbytes.batm.server.extensions.extra.decent.DecentExtension
import com.generalbytes.batm.common.Alias.{Attempt, Identifier, Task}
import org.http4s.Uri

import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Try

object Main /*extends App*/ {
  import Util._
  import com.generalbytes.batm.common.Currency._

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

  def runClient(): Unit = {
    println(s"Running as CLIENT\r\nURL is $host")
    val client = new ApiClient(
      Uri.unsafeFromString("https://10.0.2.15/apiv2/"),
      "VT123456",
      "P4RPIUO4ICDXVV3DV84PVUXOOMVBEEZ7R",
      "SVPMYSUXS7URIRID3S7AIVD8THSEM7XOM"
    )
    val currencyPair = CurrencyPair(Euro, Decent)
    val resultIO = client.purchase(currencyPair, 1000L, "blahblah")
    val txId = resultIO.unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .map(_.data.result.remoteTransactionId)
      .logError
      .getOrThrow

    println(s"txid: $txId")
  }

  def runServer(): Unit = {

  }

  runClient()
}
