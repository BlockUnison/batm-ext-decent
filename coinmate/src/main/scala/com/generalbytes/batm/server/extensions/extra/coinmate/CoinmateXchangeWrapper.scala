package com.generalbytes.batm.server.extensions.extra.coinmate

import cats.effect.ConcurrentEffect
import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.utils.Util._
import com.generalbytes.batm.common.utils.XChangeUtils._
import com.generalbytes.batm.common.utils.LoggingSupport
import org.knowm.xchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.coinmate.CoinmateExchange
import org.knowm.xchange.dto.Order.OrderType
import org.knowm.xchange.dto.trade.MarketOrder
import retry.Sleep

case class LoginInfo(apiKey: String, secretKey: String)

class CoinmateXchangeWrapper [F[_]: Sleep : ConcurrentEffect](credentials: LoginInfo)
  extends Exchange[F] with LoggingSupport {

  protected val exchange: xchange.Exchange = createExchange

  protected def createExchange: xchange.Exchange = {
    val spec = new CoinmateExchange().getDefaultExchangeSpecification
    spec.setApiKey(credentials.apiKey)
    spec.setSecretKey(credentials.secretKey)
    val result = ExchangeFactory.INSTANCE.createExchange(spec)
    result
  }

  override def getBalance(currency: Currency): F[Amount] = delay {
    exchange.getAccountService.getAccountInfo.getWallet.getBalance(currency.convert).getTotal |> BigDecimal.apply
  }

  override def getAddress(currency: Currency): F[Address] = raise[F](err"Not implemented")

  override def fulfillOrder(order: TradeOrder): F[Identifier] = delay {
    val marketOrder = new MarketOrder.Builder(getOrderType(order), order.currencyPair.convert)
      .originalAmount(order.amount.amount.bigDecimal)
      .build()
    exchange.getTradeService.placeMarketOrder(marketOrder)
  }

  override def withdrawFunds(currency: Currency, amount: Amount, destination: Address): F[Identifier] = delay {
    exchange.getAccountService.withdrawFunds(currency.convert, amount.bigDecimal, destination)
  }

  override val cryptoCurrencies: Set[CryptoCurrency] = Set(Currency.BitcoinCash, Currency.Bitcoin)
  override val fiatCurrencies: Set[FiatCurrency] = Set(Currency.Euro)
  override val preferredFiat: FiatCurrency = Currency.Euro
}
