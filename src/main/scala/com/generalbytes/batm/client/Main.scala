package com.generalbytes.batm.client

import cats.Show
import cats.implicits._
import com.generalbytes.batm.common.Alias.Task
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.{Currency, Util}
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.{DefaultBittrexXChangeWrapper, SubstitutingBittrexXChangeWrapper}
import com.generalbytes.batm.server.extensions.extra.decent.extension.LoginInfo
import org.knowm.xchange.dto.trade.UserTrade
import org.slf4j.{Logger, LoggerFactory}

import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Try

object Main extends App {

  implicit val logger: Logger = LoggerFactory.getLogger("Main")

  val walletHost = "207.154.255.239:9696"

  def runDirect(): Unit = {
    implicit val userTradeShow: Show[UserTrade] = Show.fromToString
    val credentials = LoginInfo("ecfd6e9a0a45480e8d695ae70912319f", "2367ac62c29440f5a758b90a7ec1e0e4")
    val exchange = new SubstitutingBittrexXChangeWrapper(new DefaultBittrexXChangeWrapper[Task](credentials), Currency.Bitcoin)

//    val res = exchange.getTrades.attempt.unsafeRunSync().log
//    res.left.foreach(println)
//    res.getOrThrow.sortBy(_.getTimestamp).mkString("\r\n") |> println
  }

  val apiHost = "https://10.0.2.15/apiv2/"

  val terminalSerialNumber = "VT123456"

  val apiKey = "PD8AEO3M8EMDTVS8D3YAHBDO72XAXX27H"

  val secretKey = "2367ac62c29440f5a758b90a7ec1e0e4"

  def runHmac(): Unit = {
    println("Signature generator")
//    val quantityDef = 0.002
//    print(s"Quantity ($quantityDef): ")
//    val quantity = StdIn.readLine().some.flatMap(x => Try(x.toDouble).toOption).getOrElse(quantityDef)
//    print(s"Api key ($apiKey): ")
//    val apiKeyActual = StdIn.readLine().some.filter(_.nonEmpty).map(_.trim).getOrElse(apiKey)
    print(s"Secret key ($secretKey): ")
    val secretKeyActual = StdIn.readLine().some.filter(_.nonEmpty).map(_.trim).getOrElse(secretKey)

    val nonce = System.currentTimeMillis

    val uri = s"https://bittrex.com/api/v1.1/account/getorderhistory?apikey=ecfd6e9a0a45480e8d695ae70912319f&nonce=$nonce"

    val signature = Util.hmacsha512(uri, secretKeyActual).toLowerCase
    println(s"Uri: $uri")
    println(s"Signature: $signature")
  }

  def run(): Unit = {
    println("Press 'v' for virtual terminal client, 'h' for hmac generation, or 'c' for direct wallet client")
    val resp = StdIn.readLine().toLowerCase.trim
    if(resp == "h") {
      runHmac()
    } else if(resp == "c") {
      runDirect()
    } else return
    run()
  }

  run()
}
