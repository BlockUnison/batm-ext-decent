package com.generalbytes.batm.common.adapters

import java.util

import cats.{Applicative, Id, ~>}
import com.generalbytes.batm.common.domain.{Attempt, Extension, Interpreter}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.utils.LoggingSupport
import com.generalbytes.batm.server.extensions._
import com.generalbytes.batm.server.extensions.watchlist.IWatchList

class ExtensionAdapter[F[_]: Applicative : Interpreter](ext: Extension)(implicit val g: Attempt ~> Id)
  extends IExtension with LoggingSupport {

  override def getName: String = ext.name

  override def getSupportedCryptoCurrencies: util.Set[String] = ext.supportedCryptoCurrencies.map(_.name).toJavaSet

  override def createExchange(loginInfo: String): IExchange = g(ext.createExchange(loginInfo))

  override def createPaymentProcessor(s: String): IPaymentProcessor = null

  override def createRateSource(loginInfo: String): IRateSource = g(ext.createRateSource(loginInfo))

  override def createWallet(loginInfo: String): IWallet = g(ext.createWallet(loginInfo))

  override def createAddressValidator(cryptoCurrency: String): ICryptoAddressValidator =
    g(ext.createAddressValidator)

  override def createPaperWalletGenerator(s: String): IPaperWalletGenerator = null

  override def getSupportedWatchListsNames: util.Set[String] = Set.empty[String].toJavaSet

  override def getWatchList(s: String): IWatchList = null

  override def init(iExtensionContext: IExtensionContext): Unit = ()

  override def getCryptoCurrencyDefinitions: util.Set[ICryptoCurrencyDefinition] = Set.empty[ICryptoCurrencyDefinition].toJavaSet

  override def getRestServices: util.Set[IRestService] = Set.empty[IRestService].toJavaSet
}
