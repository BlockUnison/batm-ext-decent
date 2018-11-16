package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.effect.{ConcurrentEffect, Effect}
import cats.instances.list._
import cats.syntax.eq._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.semigroup._
import cats.syntax.traverse._
import cats.{Applicative, Eval, Later, Monad, Now, Semigroup}
import com.generalbytes.batm.common.domain.{ErrorApplicative, ExchangeRate}
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.utils.LoggingSupport
import com.generalbytes.batm.common.utils.Util._
import com.generalbytes.batm.server.extensions.extra.decent.utils.BittrexUtils._
import org.knowm.xchange.dto.Order.OrderType

class BittrexWrapperRateSource[F[_]: ConcurrentEffect](intermediate: List[Currency])
  extends RateSource[F] with LoggingSupport {
  implicit val bigDecimalMulSemigroup: Semigroup[ExchangeRate] = (x: ExchangeRate, y: ExchangeRate) => x * y

  private def createTicker(orderType: OrderType, currencyPair: CurrencyPair): Eval[F[ExchangeRate]] = {
    if (currencyPair.base === currencyPair.counter)
      Now(Applicative[F].pure(BigDecimal.valueOf(1L)))
    else if (currencyPair.base.isInstanceOf[Fiat] && currencyPair.counter.isInstanceOf[Fiat])
      Later(new FiatCurrencyExchangeTicker[F](currencyPair)).map(_.currentRate)
    else
      Later(new FallbackBittrexTicker[F](currencyPair)).map(_.currentRates.map(getRateSelector(orderType)))
  }

  def getCurrencySteps(currencyPair: CurrencyPair, between: List[Currency]): List[Currency] =
    currencyPair.counter :: (currencyPair.base :: between.reverse).reverse

  private def getRate(orderType: OrderType, currencyPair: CurrencyPair): F[ExchangeRate] = {
    val progression = getCurrencySteps(currencyPair, intermediate)

    val currencyPairs = (progression zip progression.tail).map((CurrencyPair.apply _).tupled).filter(cp => cp.base =!= cp.counter)
    logger.debug(currencyPairs.map(_.toString).mkString(" "))

    val rates = for {
      cp <- currencyPairs
    } yield createTicker(orderType, cp).value

    for {
      rs <- rates.sequence
    } yield rs.foldLeft(BigDecimal.valueOf(1L))(_ |+| _)
  }

  override def getExchangeRateForSell(currencyPair: CurrencyPair): F[ExchangeRate] =
    getRate(OrderType.BID, currencyPair)

  override def getExchangeRateForBuy(currencyPair: CurrencyPair): F[ExchangeRate] =
    getRate(OrderType.ASK, currencyPair)

  override val cryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent)
  override val fiatCurrencies: Set[FiatCurrency] = Set(Currency.Euro)
  override val preferredFiat: FiatCurrency = Currency.Euro
}
