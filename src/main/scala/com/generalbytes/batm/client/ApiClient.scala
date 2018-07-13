package com.generalbytes.batm.client

import cats.effect.IO
import com.generalbytes.batm.common.Alias.{Address, Amount}
import com.generalbytes.batm.common.{Currency, CurrencyPair, Util}
import org.http4s.{Response => _, _}
import org.http4s.client.blaze._

case class Money[T <: Currency](amount: Amount)

class ApiClient(val baseUri: Uri, val serialNumber: String, apiKey: String, secretKey: String) {

//  implicit val decoder: Decoder[ApiResponse] = deriveDecoder[ApiResponse]

  private def purchaseUri(request: PurchaseRequest): Uri =
    baseUri.withPath("private/buy_crypto")
      .withQueryParam("nonce", request.nonce)
      .withQueryParam("api_key", request.apiKey)
      .withQueryParam("signature", request.signature)
      .withQueryParam("fiat_amount", request.fiatAmount)
      .withQueryParam("fiat_currency", request.fiatCurrency)
      .withQueryParam("crypto_amount", request.cryptoAmount)
      .withQueryParam("crypto_currency", request.cryptoCurrency)
      .withQueryParam("destination_address", request.destinationAddress)


  type ApiResponse = Response[Result[PurchaseResponse]]

  def purchase[F <: Currency, T <: Currency](currencyPair: CurrencyPair[F, T], amount: Amount, address: Address): IO[ApiResponse] = {
    val nonce = System.currentTimeMillis / 1000
    val secret = Util.hmacsha256(nonce.toString+serialNumber+apiKey, secretKey)
    val request = PurchaseRequest(serialNumber, nonce, apiKey, secret, amount, currencyPair.from.name, 0L, currencyPair.to.name, address)
    val uri = purchaseUri(request)

    val client = Http1Client[IO]().unsafeRunSync()
//    client.expect[ApiResponse](uri)

    IO.pure(Response("ok", null, Result(PurchaseResponse(0L, Currency.Euro.name, address, amount, Currency.Decent.name, "", "", 1))))
  }
}
