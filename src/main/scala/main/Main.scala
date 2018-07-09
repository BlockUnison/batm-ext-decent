package main

import cats.effect._
import cats.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.DecentExtension
import common.Alias.{Attempt, Identifier, Task}
import common.Util

import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Try

object Main /*extends App*/ {
  import Util._

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
    request()
  }

  def runServer(): Unit = {

  }

  runClient()
}
