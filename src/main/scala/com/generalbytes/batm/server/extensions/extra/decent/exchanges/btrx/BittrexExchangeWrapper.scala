package com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx

import java.math.BigDecimal
import java.util

import com.generalbytes.batm.common.Alias.{Address, Identifier}
import com.generalbytes.batm.common.Currency
import com.generalbytes.batm.server.extensions.{IExchangeAdvanced, ITask}

class BittrexExchangeWrapper(exch: IExchangeAdvanced) extends IExchangeAdvanced {
  override def getCryptoCurrencies: util.Set[String] = exch.getCryptoCurrencies

  override def getFiatCurrencies: util.Set[String] = exch.getFiatCurrencies

  override def getPreferredFiatCurrency: String = exch.getPreferredFiatCurrency

  override def getCryptoBalance(currency: String): BigDecimal = exch.getCryptoBalance(currency)

  override def getFiatBalance(currency: String): BigDecimal = exch.getFiatBalance(currency)

  override def purchaseCoins(amount: BigDecimal, cryptoCurrency: String, fiatCurrency: String, description: String): Identifier = {
    exch.purchaseCoins(amount, cryptoCurrency, Currency.USDollar.name, description)
  }

  override def sellCoins(amount: BigDecimal, cryptoCurrency: String, fiatCurrency: String, description: String): Identifier =
    exch.sellCoins(amount, cryptoCurrency, Currency.USDollar.name, description)

  override def sendCoins(destinationAddress: Address, amount: BigDecimal, cryptoCurrency: String, description: String): Identifier =
    exch.sendCoins(destinationAddress, amount, cryptoCurrency, description)

  override def getDepositAddress(currency: String): Address = exch.getDepositAddress(currency)

  override def createPurchaseCoinsTask(amount: BigDecimal, cryptoCurrency: String, fiatCurrency: String, description: String): ITask =
    exch.createPurchaseCoinsTask(amount, cryptoCurrency, Currency.USDollar.name, description)

  override def createSellCoinsTask(amount: BigDecimal, cryptoCurrency: String, fiatCurrency: String, description: String): ITask =
    exch.createSellCoinsTask(amount, cryptoCurrency, Currency.USDollar.name, description)

}
