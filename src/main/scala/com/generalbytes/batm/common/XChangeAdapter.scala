package com.generalbytes.batm.common

import java.util

import com.generalbytes.batm.server.extensions.extra.common.XChangeExchange
import com.generalbytes.batm.server.extensions.{IExchangeAdvanced, IRateSourceAdvanced, ITask}

class XChangeAdapter[T <: XChangeExchange](xch: T) extends IExchangeAdvanced with IRateSourceAdvanced {
  override def createPurchaseCoinsTask(bigDecimal: java.math.BigDecimal, s: String, s1: String, s2: String): ITask = xch.createPurchaseCoinsTask(bigDecimal,s, s1, s2)

  override def createSellCoinsTask(bigDecimal: java.math.BigDecimal, s: String, s1: String, s2: String): ITask = xch.createSellCoinsTask(bigDecimal, s, s1, s2)

  override def getExchangeRateForBuy(s: String, s1: String): java.math.BigDecimal = xch.getExchangeRateForBuy(s, s1)

  override def getExchangeRateForSell(s: String, s1: String): java.math.BigDecimal = xch.getExchangeRateForSell(s, s1)

  override def calculateBuyPrice(s: String, s1: String, bigDecimal: java.math.BigDecimal): java.math.BigDecimal = xch.calculateBuyPrice(s, s1, bigDecimal)

  override def calculateSellPrice(s: String, s1: String, bigDecimal: java.math.BigDecimal): java.math.BigDecimal = xch.calculateSellPrice(s, s1, bigDecimal)

  override def getCryptoBalance(s: String): java.math.BigDecimal = xch.getCryptoBalance(s)

  override def getFiatBalance(s: String): java.math.BigDecimal = xch.getFiatBalance(s)

  override def purchaseCoins(bigDecimal: java.math.BigDecimal, s: String, s1: String, s2: String): String = xch.purchaseCoins(bigDecimal, s, s1, s2)

  override def sellCoins(bigDecimal: java.math.BigDecimal, s: String, s1: String, s2: String): String = xch.sellCoins(bigDecimal, s, s1, s2)

  override def sendCoins(s: String, bigDecimal: java.math.BigDecimal, s1: String, s2: String): String = xch.sendCoins(s, bigDecimal, s1, s2)

  override def getDepositAddress(s: String): String = xch.getDepositAddress(s)

  override def getCryptoCurrencies: util.Set[String] = xch.getCryptoCurrencies

  override def getFiatCurrencies: util.Set[String] = xch.getFiatCurrencies

  override def getExchangeRateLast(s: String, s1: String): java.math.BigDecimal = xch.getExchangeRateLast(s, s1)

  override def getPreferredFiatCurrency: String = xch.getPreferredFiatCurrency
}
