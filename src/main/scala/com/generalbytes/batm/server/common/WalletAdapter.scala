package com.generalbytes.batm.server.common

import java.math.BigDecimal
import java.util

import com.generalbytes.batm.server.extensions.IWallet
import com.typesafe.scalalogging.Logger
import Alias.Task
import Currency.Default

import scala.collection.JavaConverters._
import scala.collection.mutable

class WalletAdapter[T <: Currency : Default](wallet: Wallet[T, Task]) extends IWallet {
  import Util._
  implicit val logger: Logger = Logger[WalletAdapter[T]]

  override def sendCoins(address: String, amount: BigDecimal, currency: String, desc: String): String = {
    val result = wallet.issuePayment(address, amount.toBigInteger.longValue, desc)
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
    log(result).getOrElse("")
  }

  override def getCryptoAddress(s: String): String = {
    val result = wallet.getAddress
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
    log(result).getOrElse("")
  }

  override def getCryptoCurrencies: util.Set[String] = mutable.Set(Currency[T].name).asJava

  override def getPreferredCryptoCurrency: String = Currency[T].name

  override def getCryptoBalance(s: String): java.math.BigDecimal = {
    val result = wallet.getBalance
      .unsafeRunTimed(defaultDuration)
      .toRight("Request timeout")
      .flatMap(identity)
      .map(BigDecimal.valueOf)
    log(result).getOrElse(BigDecimal.valueOf(-1L))
  }
}
