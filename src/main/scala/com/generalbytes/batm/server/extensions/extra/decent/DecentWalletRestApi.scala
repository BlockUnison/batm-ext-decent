package com.generalbytes.batm.server.extensions.extra.decent

import cats.effect.IO
import cats.implicits._
import com.generalbytes.batm.common.Currency
import com.generalbytes.batm.server.extensions.extra.decent.DecentAlias.WalletApi
import com.generalbytes.batm.common.Alias._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
//import org.http4s.circe.CirceEntityDecoder._
import io.circe.parser._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._

case class DecentRequest(username: String, password: String, amount: Amount, address: String)
case class TransactionInfo(amountLeftInWallet: Amount, txid: Identifier, blockNum: BlockNumber)

case class DecentResponse(b: DecentRequest, r: TransactionInfo)

trait WalletCredentials[T <: Currency]    // TODO: Remove
case class DecentWalletCredentials(username: String, password: String) extends WalletCredentials[Currency.DCT]

class DecentWalletRestApi(url: Uri, credentials: DecentWalletCredentials) extends WalletApi {
  import scala.concurrent.ExecutionContext.Implicits.global

  override def issuePayment(recipientAddress: Address, amount: Amount, description: String = ""): Task[Identifier] = {
    val requestJson = DecentRequest(credentials.username, credentials.password, amount, recipientAddress)
    val request = POST (
      url,
      requestJson.asJson
    )
    Http1Client[IO]()
      .flatMap(client => client.expect[String](request))    // TODO: Get the decoder to work!
      .map(parse)
      .map(_.leftMap(_.toString))
      .map(_.flatMap(parseTransactionId))
  }

  private def parseTransactionId(json: Json): Attempt[Identifier] = {
    val txIdOpt = for {
      obj <- json.asObject
      rObj <- obj("r")
      r <- rObj.asObject
      txid <- r("txid")
      res <- txid.asString
    } yield res
    txIdOpt.toRight("Could not parse transaction ID from response")
  }

  override def getBalance: Task[Amount] = IO("Not implemented".asLeft[Amount])

  override def getAddress: Task[Address] = IO("Not implemented".asLeft[Address])
}
