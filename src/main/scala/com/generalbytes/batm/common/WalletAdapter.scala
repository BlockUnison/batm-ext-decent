package com.generalbytes.batm.common

import java.math.BigDecimal
import java.util

import Adapters._
import com.generalbytes.batm.server.extensions.IWallet
import com.typesafe.scalalogging.Logger
import Currency.Default
import com.generalbytes.batm.common.Alias.Task

import scala.collection.JavaConverters._
import scala.collection.mutable

class WalletAdapter[T <: Currency : Default](wallet: Wallet[T, Task]) extends IWallet {
  import Util._
  implicit val logger: Logger = Logger[WalletAdapter[T]]

  override def sendCoins(address: Address, amount: Amount, currency: String, desc: String): TransactionId = {
    wallet.issuePayment(address, amount.toBigInteger.longValue, desc)
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .logError
      .getOrElse("")
  }

  override def getCryptoAddress(s: String): Address = {
    wallet.getAddress
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .logError
      .getOrElse("")
  }

  override def getCryptoCurrencies: util.Set[String] = mutable.Set(Currency[T].name).asJava

  override def getPreferredCryptoCurrency: String = Currency[T].name

  override def getCryptoBalance(s: String): BigDecimal = {
    wallet.getBalance
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .map(BigDecimal.valueOf)
      .logError
      .getOrElse(BigDecimal.valueOf(-1L))
  }
}
