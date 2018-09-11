package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.Amount

final case class CryptoAmount[T <: Currency](amount: Amount)

sealed trait TradeOrder[T <: Currency] {
  val currencyPair: CurrencyPair
  val amount: CryptoAmount[T]

  def inverse: TradeOrder[T] = this match {
    case PurchaseOrder(_, _) => SaleOrder(currencyPair, amount)
    case SaleOrder(_, _) => PurchaseOrder(currencyPair, amount)
  }
}

final case class PurchaseOrder[T <: Currency](currencyPair: CurrencyPair, amount: CryptoAmount[T]) extends TradeOrder[T]
final case class SaleOrder[T <: Currency](currencyPair: CurrencyPair, amount: CryptoAmount[T]) extends TradeOrder[T]

object TradeOrder {
  def buy[T <: Currency](cryptoCurrency: CryptoCurrency, counter: Currency, amount: Amount): TradeOrder[T] =
    PurchaseOrder(CurrencyPair(counter, cryptoCurrency), CryptoAmount[T](amount))

  def sell[T <: Currency](cryptoCurrency: CryptoCurrency, counter: Currency, amount: Amount): TradeOrder[T] =
    SaleOrder(CurrencyPair(counter, cryptoCurrency), CryptoAmount[T](amount))
}
