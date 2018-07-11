package com.generalbytes.batm.client

import com.generalbytes.batm.common.Alias.{Address, Amount}

case class PurchaseRequest(
                            serialNumber: String,
                            nonce: Long,
                            apiKey: String,
                            signature: String,
                            fiatAmount: Amount,
                            fiatCurrency: String,
                            cryptoAmount: Amount,
                            cryptoCurrency: String,
                            destinationAddress: String
                          )

case class PurchaseResponse(
                            cashAmount: Amount,
                            cashCurrency: String,
                            cryptoAddress: Address,
                            cryptoAmount: Amount,
                            cryptoCurrency: String,
                            localTransactionId: String,
                            remoteTransactionId: String,
                            status: Int
                           )

case class Result[T](result: T)

object Status {
  sealed trait StatusCode {
    val value: String
  }

  case object Ok extends StatusCode { val value = "ok" }
  case object Error extends StatusCode { val value = "error" }
}

trait Response[T] {
  val status: Status.StatusCode
  val message: Option[String]
  val data: T
}