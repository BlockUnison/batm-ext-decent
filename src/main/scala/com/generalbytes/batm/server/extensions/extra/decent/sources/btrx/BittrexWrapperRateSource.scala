package com.generalbytes.batm.server.extensions.extra.decent.sources.btrx

import cats.effect.Effect
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.semigroup._
import cats.{Monad, Semigroup}
import com.generalbytes.batm.common.Alias.{ApplicativeErr, ExchangeRate}
import com.generalbytes.batm.common._

class BittrexWrapperRateSource[F[_]: Effect : Monad : ApplicativeErr] extends RateSource[F] with LoggingSupport {
  implicit val bigDecimalMulSemigroup: Semigroup[ExchangeRate] = (x: ExchangeRate, y: ExchangeRate) => x * y

  private val fiatExRSource = new FiatCurrencyExchangeRateSource[F](Currency.Euro, Currency.USDollar)
  private val usd2btcTicker = new BittrexTicker[F](CurrencyPair(Currency.USDollar, Currency.Bitcoin))
  private val btc2dctTicker = new BittrexTicker[F](CurrencyPair(Currency.Bitcoin, Currency.Decent))

  private def getRate(f: BittrexTick => ExchangeRate): F[ExchangeRate] = {
    for {
      fiat <- fiatExRSource.currentRate
      usd2btc <- usd2btcTicker.currentRates.map(f)
      btc2dct <- btc2dctTicker.currentRates.map(f)
    } yield fiat |+| usd2btc |+| btc2dct
  }

  override def getExchangeRateForSell(currencyPair: CurrencyPair): F[BigDecimal] =
    getRate(_.bid)

  override def getExchangeRateForBuy(currencyPair: CurrencyPair): F[BigDecimal] =
    getRate(_.ask)

  override val cryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent)
  override val fiatCurrencies: Set[FiatCurrency] = Set(Currency.Euro)
  override val preferredFiat: FiatCurrency = Currency.Euro
}
