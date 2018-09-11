package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.{Address, Amount, Identifier}

trait Exchange[F[_]] extends ExchangeBase {
  def getBalance[T <: Currency](currency: T): F[Amount]
  def getAddress[T <: Currency](currency: T): F[Address]
  def fulfillOrder[T <: Currency](order: TradeOrder[T]): F[Identifier]
  def withdrawFunds[T <: Currency](currency: Currency, amount: Amount, destination: Address): F[Identifier]
}
