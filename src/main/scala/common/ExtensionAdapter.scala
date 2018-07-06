package common

import java.util

import com.generalbytes.batm.server.extensions._
import com.generalbytes.batm.server.extensions.watchlist.IWatchList
import common.Currency.Default

import scala.collection.mutable


class ExtensionAdapter[T <: Currency : Default](ext: Extension[T]) extends IExtension {
  import scala.collection.JavaConverters._

  override def getName: String = ext.name

  override def getSupportedCryptoCurrencies: util.Set[String] = mutable.Set(ext.supportedCryptoCurrencies.map(_.name).toList: _*).asJava

  override def createExchange(s: String): IExchange = null

  override def createPaymentProcessor(s: String): IPaymentProcessor = null

  override def createRateSource(s: String): IRateSource = null

  override def createWallet(s: String): IWallet = ext.walletBuilder(s).map(new WalletAdapter(_)).orNull

  override def createAddressValidator(s: String): ICryptoAddressValidator = null

  override def createPaperWalletGenerator(s: String): IPaperWalletGenerator = null

  override def getSupportedWatchListsNames: util.Set[String] = null

  override def getWatchList(s: String): IWatchList = null
}


object ExtensionAdapter {
  def create[T <: Currency : Default](ext: Extension[T]): ExtensionAdapter[T] = new ExtensionAdapter[T](ext)
}