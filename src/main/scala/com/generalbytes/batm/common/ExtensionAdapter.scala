package com.generalbytes.batm.common

import java.util

import com.generalbytes.batm.server.extensions._
import com.generalbytes.batm.server.extensions.watchlist.IWatchList
import Currency.Default
import Util._
import com.typesafe.scalalogging.Logger

class ExtensionAdapter[T <: Currency : Default](ext: Extension[T]) extends IExtension {
  implicit val logger: Logger = Logger[this.type]

  override def getName: String = ext.name

  override def getSupportedCryptoCurrencies: util.Set[String] = ext.supportedCryptoCurrencies.map(_.name).toJavaSet

  override def createExchange(loginInfo: String): IExchange = ext.createExchange(loginInfo).logError.getOrNull

  override def createPaymentProcessor(s: String): IPaymentProcessor = null

  override def createRateSource(loginInfo: String): IRateSource = ext.createRateSource(loginInfo).logError.getOrNull

  override def createWallet(loginInfo: String): IWallet =
    ext.createWallet(loginInfo)
      .map(new WalletAdapter(_))
      .logError
      .getOrNull

  override def createAddressValidator(s: String): ICryptoAddressValidator = null

  override def createPaperWalletGenerator(s: String): IPaperWalletGenerator = null

  override def getSupportedWatchListsNames: util.Set[String] = null

  override def getWatchList(s: String): IWatchList = null
}
