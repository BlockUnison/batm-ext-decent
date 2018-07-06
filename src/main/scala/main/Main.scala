package main

import cats.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.DecentExtension

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}

object Main extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

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
    val result = new DecentExtension().walletBuilder(loginStr).map(_.issuePayment(address, amount))
    Try(Await.result(result.sequence, 5 seconds)) match {
      case Success(_) => println("Success!")
      case Failure(e) => println(s"Failure: $e")
    }
  }

  def runClient(): Unit = {

    println(s"Running as CLIENT\r\nURL is $host")
    request()
  }

  def runServer(): Unit = {

  }

  runClient()
}
