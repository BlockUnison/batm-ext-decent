package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.effect.ConcurrentEffect
import cats.implicits._
import com.generalbytes.batm.common.Alias.{Attempt, ExchangeRate}
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.{ClientFactory, CurrencyPair, LoggingSupport}
import io.circe.{Decoder, DecodingFailure}
import io.circe._
import io.circe.syntax._
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._

case class BittrexTick(bid: ExchangeRate, ask: ExchangeRate, last: ExchangeRate)
case class BittrexTickError(message: String) extends Throwable(message)

class BittrexTicker[F[_]: ConcurrentEffect](currencyPair: CurrencyPair) extends LoggingSupport with ClientFactory[F] {
  import BittrexTicker._
  private val uri: Uri = Uri.unsafeFromString("https://bittrex.com/api/v1.1/public/getticker")
      .withQueryParam("market", currencyPair.toString)

  def currentRates: F[BittrexTick] =
    client
      .flatMap(_.expect[Attempt[BittrexTick]](uri))
      .flatTap(x => log(x, currencyPair.toString))
      .unattempt
}

object BittrexTicker {
  def failure[A]: Decoder.Result[A] = DecodingFailure("Failure", Nil).asLeft[A]
  val tickDecoder: Decoder[BittrexTick] = Decoder.forProduct3("Bid", "Ask", "Last")(BittrexTick)

  implicit val bittrexTickDecoder: Decoder[BittrexTick] = Decoder.instance { c =>
    c.get[Boolean]("success")
      .ifM(
        c.downField("result").as[BittrexTick](tickDecoder),
        failure)
  }

  implicit val bittrexTickErrorDecoder: Decoder[BittrexTickError] = Decoder.instance { c =>
    c.get[Boolean]("success").map(!_)
      .ifM(
        c.downField("message").as[String].map(BittrexTickError),
        failure)
  }

  implicit val bittrexResponseDecoder: Decoder[Attempt[BittrexTick]] = (bittrexTickDecoder either bittrexTickErrorDecoder).map(_.swap)
}
