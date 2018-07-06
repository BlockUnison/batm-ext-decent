package com.generalbytes.batm.server.extensions.extra.decent

import cats.effect.IO
import com.generalbytes.batm.server.extensions.extra.decent.Decent.WalletApi
import common.Alias._
import common.Currency
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._

case class DecentRequest(username: String, password: String, amount: Amount, address: String)
case class TransactionInfo(amountLeftInWallet: Amount, txid: Identifier, blockNum: BlockNumber)

case class DecentResponse(b: DecentRequest, r: TransactionInfo)

trait WalletCredentials[T <: Currency]
case class DecentWalletCredentials(username: String, password: String) extends WalletCredentials[Currency.DCT]

class DecentWalletRestApi(url: Uri, credentials: DecentWalletCredentials) extends WalletApi {
  import scala.concurrent.ExecutionContext.Implicits.global

  override def issuePayment(recipientAddress: Address, amount: Amount, description: String = ""): Task[Identifier] = {
    val requestJson = DecentRequest(credentials.username, credentials.password, amount, recipientAddress)
    val request = POST (
      url,
      requestJson.asJson
    )
    Http1Client[IO]().flatMap(client => client.expect[DecentResponse](request)).unsafeToFuture().map(t => "")
  }

  override def getBalance: Task[Amount] = ???

  override def getAddress: Task[Address] = ???
}
