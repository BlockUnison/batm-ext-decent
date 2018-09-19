package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.Monad
import cats.implicits._
import cats.effect.{Effect, Sync}
import com.generalbytes.batm.common.Alias.ApplicativeErr
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common.CurrencyPair

class FallbackBittrexTicker[F[_] : Effect : Sync : Monad : ApplicativeErr](currencyPair: CurrencyPair) {
  val underlying: BittrexTicker[F] = new BittrexTicker[F](currencyPair)
  lazy val inverse: BittrexTicker[F] = new BittrexTicker[F](currencyPair.flip)

  def currentRates: F[BittrexTick] = underlying.currentRates.handleErrorWith {
    case BittrexTickError("INVALID_MARKET") => inverse.currentRates.map(flipTick)
    case e => raise[F](e)
  }

  private def flipTick(tick: BittrexTick): BittrexTick = BittrexTick(1 / tick.ask, 1 / tick.bid, tick.last)
}
