package com.generalbytes.batm.common

import java.util

import com.generalbytes.batm.server.extensions._
import com.generalbytes.batm.server.extensions.watchlist.IWatchList
import Currency.Default
import Util._

class ExtensionAdapter[T <: Currency : Default](ext: Extension[T]) extends IExtension {

  override def getName: String = ext.name

  override def getSupportedCryptoCurrencies: util.Set[String] = ext.supportedCryptoCurrencies.map(_.name).toJavaSet

  override def createExchange(loginInfo: String): IExchange = ext.createExchange(loginInfo).getOrThrow

  override def createPaymentProcessor(s: String): IPaymentProcessor = null

  override def createRateSource(s: String): IRateSource = ext.createRateSource.getOrThrow

  override def createWallet(loginInfo: String): IWallet = ext.createWallet(loginInfo).map(new WalletAdapter(_)).getOrThrow

  override def createAddressValidator(s: String): ICryptoAddressValidator = null

  override def createPaperWalletGenerator(s: String): IPaperWalletGenerator = null

  override def getSupportedWatchListsNames: util.Set[String] = null

  override def getWatchList(s: String): IWatchList = null
}

object ExtensionAdapter {
  def create[T <: Currency : Default](ext: Extension[T]): ExtensionAdapter[T] = new ExtensionAdapter[T](ext)
}