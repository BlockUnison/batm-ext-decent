package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.effect.{ConcurrentEffect, Effect}
import cats.instances.list._
import cats.syntax.eq._
import cats.syntax.functor._
import cats.syntax.semigroup._
import cats.syntax.traverse._
import cats.{Applicative, Eval, Later, Monad, Semigroup}
import com.generalbytes.batm.common.Alias.{ApplicativeErr, ExchangeRate}
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.XChangeConversions._
import org.knowm.xchange.dto.Order.OrderType

class BittrexWrapperRateSource[F[_]: Effect : Monad : Applicative : ApplicativeErr : ConcurrentEffect](intermediate: List[Currency])
  extends RateSource[F] with LoggingSupport {
  implicit val bigDecimalMulSemigroup: Semigroup[ExchangeRate] = (x: ExchangeRate, y: ExchangeRate) => x * y

  private def createTicker(orderType: OrderType, currencyPair: CurrencyPair): Eval[F[ExchangeRate]] = {
    if (currencyPair.base.isInstanceOf[Fiat] && currencyPair.counter.isInstanceOf[Fiat])
      Later(new FiatCurrencyExchangeRateSource[F](currencyPair)).map(_.currentRate)
    else
      Later(new FallbackBittrexTicker[F](currencyPair)).map(_.currentRates.map(getRateSelector(orderType)))
  }

  def getCurrencySteps(currencyPair: CurrencyPair, between: List[Currency]): List[Currency] =
    currencyPair.counter :: (currencyPair.base :: between.reverse).reverse

  private def getRate(orderType: OrderType, currencyPair: CurrencyPair): F[ExchangeRate] = {
    val progression = getCurrencySteps(currencyPair, intermediate)

    val currencyPairs = (progression zip progression.tail).map((CurrencyPair.apply _).tupled).filter(cp => cp.base =!= cp.counter)

    val rates = for {
      cp <- currencyPairs
    } yield createTicker(orderType, cp).value

    for {
      rs <- rates.sequence
    } yield rs.foldLeft(BigDecimal.valueOf(1L))(_ |+| _)
  }

  override def getExchangeRateForSell(currencyPair: CurrencyPair): F[BigDecimal] =
    getRate(OrderType.BID, currencyPair)

  override def getExchangeRateForBuy(currencyPair: CurrencyPair): F[BigDecimal] =
    getRate(OrderType.ASK, currencyPair)

  override val cryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent)
  override val fiatCurrencies: Set[FiatCurrency] = Set(Currency.Euro)
  override val preferredFiat: FiatCurrency = Currency.Euro
}
