package com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx

import cats.Monad
import cats.effect.{ConcurrentEffect, Sync}
import com.generalbytes.batm.common.Alias.{ApplicativeErr, Identifier}
import com.generalbytes.batm.common.{Currency, Exchange, LoggingSupport, TradeOrder}
import com.generalbytes.batm.common.adapters.ExchangeAdapterDecorator
import shapeless._

class ReplacementBittrexXChangeWrapper[F[_]: Sync : ApplicativeErr : Monad : ConcurrentEffect](exchange: Exchange[F], currency: Currency)
  extends ExchangeAdapterDecorator[F](exchange) with LoggingSupport {
  private def counterLens[T <: Currency] = lens[TradeOrder[T]].currencyPair.counter

  override def fulfillOrder[T <: Currency](order: TradeOrder[T]): F[Identifier] = {
    val newOrder = counterLens.set(order)(currency)
    exchange.fulfillOrder(newOrder)
  }
}
