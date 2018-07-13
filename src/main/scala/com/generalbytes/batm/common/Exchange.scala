package com.generalbytes.batm.common

import Alias.{Address, Identifier, Task}

trait Exchange extends ExchangeBase {
  def getBalance[T <: Currency]: Task[BigDecimal]
  def getAddress[T <: Currency]: Task[Address]
  def fulfillOrder[T <: Currency](order: Order[T]): Task[Identifier]
}
