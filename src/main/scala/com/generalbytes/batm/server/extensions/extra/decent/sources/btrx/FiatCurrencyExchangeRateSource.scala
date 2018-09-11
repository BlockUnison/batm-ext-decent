package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.Monad
import cats.effect.{Effect, Sync}
import cats.implicits._
import com.generalbytes.batm.common.Alias.ExchangeRate
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common.{FiatCurrency, LoggingSupport}
import io.circe.Decoder
import org.http4s.Uri
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client.blaze.Http1Client

class FiatCurrencyExchangeRateSource[F[_]: Effect : Sync : Monad](from: FiatCurrency, to: FiatCurrency) extends LoggingSupport {
  import FiatCurrencyExchangeRateSource._

  private val descriptor: String = (from.name :: to.name :: Nil).mkString("_")
  private val uriBase: Uri = Uri.unsafeFromString("http://free.currencyconverterapi.com/api/v5/convert")
  private implicit val rateTickDecoder: Decoder[ExchangeRateTick] = getDecoder(descriptor)

  def currentRate: F[ExchangeRate] = {
    val uri = uriBase.withQueryParam("q", descriptor).withQueryParam("compact", "y")

    Http1Client[F]()
      .flatMap(_.expect[ExchangeRateTick](uri))
      .map(_.rate)
      .flatTap(x => log(x))
  }
}

object FiatCurrencyExchangeRateSource {
  case class ExchangeRateTick(rate: ExchangeRate)

  def getDecoder(descriptor: String): Decoder[ExchangeRateTick] = Decoder.instance { c =>
    c.downField(descriptor).get[BigDecimal]("val").map(ExchangeRateTick.apply)
  }
}
