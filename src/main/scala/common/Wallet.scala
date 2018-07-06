package common

import common.Alias.{Address, Amount, Identifier}

import scala.language.higherKinds

trait Wallet[T <: Currency, F[_]] {
  def getAddress: F[Address]
  def issuePayment(recipientAddress: Address, amount: Amount, description: String = ""): F[Identifier]
  def getBalance: F[Amount]
}
