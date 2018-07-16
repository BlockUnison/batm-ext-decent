package com.generalbytes.batm.server.extensions.extra.decent

import cats.implicits._
import com.generalbytes.batm.common.Alias.{Attempt, Task}
import com.generalbytes.batm.common.{CryptoCurrency, Currency, Extension, Wallet, _}
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex.BittrexXchange
import com.generalbytes.batm.server.extensions.{IExchangeAdvanced, IRateSourceAdvanced}
import org.http4s.Uri

// TODO: Memoize rate source when same as exchange?
class DecentExtension extends Extension[Currency.DCT] {

  case class LoginInfo(apiKey: String, secretKey: String)

  override val name: String = "DCT Extension"
  override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent)

  private val walletLoginData = """dctd:(https?):([A-Za-z0-9]+):([A-Za-z0-9\.]+):([A-Za-z0-9.]+)""".r
  private val exchangeLoginData = """([A-Za-z0-9]+):([A-Za-z0-9]+)""".r

  override def createWallet(loginInfo: String): Attempt[Wallet[Currency.DCT, Task]] = loginInfo match {
    case walletLoginData(protocol, user, password, hostname) => {
      val url = Uri.unsafeFromString(s"$protocol://$hostname")
      new DecentWalletRestApi(url, DecentWalletCredentials(user, password)).asRight
    }
    case _ => s"Login info ($loginInfo) did not match the expected format".asLeft
  }

  private def exchangeFromLogin(loginInfo: Option[LoginInfo]): IExchangeAdvanced with IRateSourceAdvanced = {
   val spec = BittrexXchange.defaultExchangeSpec
    loginInfo.foreach{ l =>
      spec.setApiKey(l.apiKey)
      spec.setSecretKey(l.secretKey)
    }
    new XChangeAdapter(new BittrexXchange(Currency.Euro, spec))
  }

  private def parseExchangeLoginInfo(loginInfo: String): Option[LoginInfo] = loginInfo match {
    case exchangeLoginData(apiKey, secretKey) => LoginInfo(apiKey, secretKey).some
    case _ => none
  }

  override def createRateSource: Attempt[IRateSourceAdvanced] = exchangeFromLogin(None).asRight

  override def createExchange(loginInfo: String): Attempt[IExchangeAdvanced] =
    exchangeFromLogin(parseExchangeLoginInfo(loginInfo)).asRight
}


