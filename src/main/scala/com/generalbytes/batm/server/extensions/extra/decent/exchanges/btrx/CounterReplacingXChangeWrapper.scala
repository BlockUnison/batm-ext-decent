package com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx

import cats._
import cats.implicits._
import cats.effect.{ConcurrentEffect, Sync}
import com.generalbytes.batm.common.Alias.{Amount, ApplicativeErr, Identifier}
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.adapters.ExchangeAdapterDecorator
import shapeless._

class CounterReplacingXChangeWrapper[F[_]: Sync : ApplicativeErr : Monad : ConcurrentEffect]
  (exchange: Exchange[F], replacement: CurrencyPair)
  extends ExchangeAdapterDecorator[F](exchange) with LoggingSupport {

  private def counterLens[T <: Currency] = lens[TradeOrder[T]].currencyPair.counter


  override def fulfillOrder[T <: Currency](order: TradeOrder[T]): F[Identifier] = {
    if (counterLens.get(order).get === replacement.counter)
      counterLens.set(order)(replacement.base) |> exchange.fulfillOrder
    else
      exchange.fulfillOrder(order)
  }

  override def getBalance[T <: Currency](currency: T): F[Amount] =
    if (currency.asInstanceOf[Currency] === replacement.counter)
      exchange.getBalance(replacement.base)
    else
      exchange.getBalance(currency)
}
