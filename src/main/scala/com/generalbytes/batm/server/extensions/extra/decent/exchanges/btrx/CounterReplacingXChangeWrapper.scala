package com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx

import cats._
import cats.implicits._
import cats.effect.{ConcurrentEffect, Sync}
import com.generalbytes.batm.common.Alias.{Amount, ApplicativeErr, Identifier}
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common.adapters.ExchangeAdapterDecorator
import shapeless._
import shapeless.syntax.std.product._

class CounterReplacingXChangeWrapper[F[_]: Sync : ApplicativeErr : Monad : ConcurrentEffect]
  (exchange: Exchange[F], replacements: Seq[CurrencyPair])
  extends ExchangeAdapterDecorator[F](exchange) with LoggingSupport {

  private val replacementMap = replacements.map(_.productElements.tupled).toMap
  private val counterLens: Prism[TradeOrder, Currency] = lens[TradeOrder].currencyPair.counter


  override def fulfillOrder(order: TradeOrder): F[Identifier] = {
    val replacementCurrency = counterLens.get(order).flatMap(replacementMap.get)
    val newOrder = replacementCurrency.map(counterLens.set(order)).getOrElse(order)
    exchange.fulfillOrder(newOrder)
  }

  override def getBalance(currency: Currency): F[Amount] ={
    val curr = currency.asInstanceOf[Currency]
    val replacementCurrency = replacementMap.get(currency)
    exchange.getBalance(replacementCurrency.getOrElse(currency))
  }
}
