package common

import java.math.BigDecimal
import java.util

import cats.Monad
import com.generalbytes.batm.server.extensions.IWallet
import common.Alias.Task
import common.Currency.Default

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class WalletAdapter[T <: Currency : Default](wallet: Wallet[T, Task]) extends IWallet {
  import Util._

  override def sendCoins(address: String, amount: BigDecimal, currency: String, desc: String): String = {
    val result = wallet.issuePayment(address, amount.toBigInteger.longValue, desc)
    Await.result(result, implicitly[Duration])   // TODO: Handle exception!!
  }

  override def getCryptoAddress(s: String): String = Await.result(wallet.getAddress, implicitly[Duration])   // TODO: Change this

  override def getCryptoCurrencies: util.Set[String] = mutable.Set(Currency[T].name).asJava

  override def getPreferredCryptoCurrency: String = Currency[T].name

  override def getCryptoBalance(s: String): java.math.BigDecimal = Await.result(wallet.getBalance, implicitly[Duration]) |> BigDecimal.valueOf
}
