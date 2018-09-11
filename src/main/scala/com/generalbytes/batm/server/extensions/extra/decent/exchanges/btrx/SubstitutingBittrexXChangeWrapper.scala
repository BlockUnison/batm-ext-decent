package com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx

import cats.Monad
import cats.effect.{Effect, Sync}
import cats.implicits._
import com.generalbytes.batm.common.Alias._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.adapters.ExchangeAdapterDecorator
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.DefaultBittrexXChangeWrapper.ErrorDecorator
import com.generalbytes.batm.server.extensions.extra.decent.sources.btrx.{BittrexTick, BittrexTicker}
import shapeless._

class SubstitutingBittrexXChangeWrapper[F[_]: Sync : ApplicativeErr : Monad : Effect](exchange: Exchange[F], midCurrency: Currency)
  extends ExchangeAdapterDecorator[F](exchange) with LoggingSupport {

  override def fulfillOrder[T <: Currency](order: TradeOrder[T]): F[Identifier] = {
    if (order.currencyPair.counter === midCurrency || order.currencyPair.base === midCurrency) exchange.fulfillOrder(order)
    else {
      val midCurrencyAmount = getAmountInMidCurrency(order)

      val result = for {
        amount <- midCurrencyAmount
        _ <- exchange.fulfillOrder(createFirstSubOrder(order, amount))
        txId <- exchange.fulfillOrder(createSecondSubOrder(order))
      } yield txId

      result.handleErrorWith {
        case e @ ErrorDecorator(_, CurrencyPair(_, mc)) if mc === midCurrency =>
          for {
            amount <- midCurrencyAmount
            undoOrder = createFirstSubOrder(order, amount).inverse
            undoTxId <- exchange.fulfillOrder(undoOrder)
          } yield undoTxId
          ApplicativeErr[F].raiseError(e)
        case e => ApplicativeErr[F].raiseError(e)
      }
    }
  }

  private def createFirstSubOrder[T <: Currency](order: TradeOrder[T], amount: Amount): TradeOrder[T] = {
    val amountLens = lens[TradeOrder[T]].amount.amount
    val currencyPairLens = lens[TradeOrder[T]].currencyPair
    val firstCP = CurrencyPair(order.currencyPair.counter, midCurrency)

    (currencyPairLens ~ amountLens).set(order)(firstCP, amount)
  }

  private def createSecondSubOrder[T <: Currency](order: TradeOrder[T]): TradeOrder[T] = {
    val secondCP = CurrencyPair(midCurrency, order.currencyPair.base)
    lens[TradeOrder[T]].currencyPair.set(order)(secondCP)
  }

  private def getAmountInMidCurrency[T <: Currency](order: TradeOrder[T]): F[Amount] = {
    import XChangeConversions._
    val bittrexTicker = new BittrexTicker[F](CurrencyPair(midCurrency, order.currencyPair.base))
    val rateSelector: BittrexTick => ExchangeRate = getRateSelector(getOrderType(order))

    for {
      rate <- bittrexTicker.currentRates
    } yield order.amount.amount * rateSelector(rate)
  }
}
