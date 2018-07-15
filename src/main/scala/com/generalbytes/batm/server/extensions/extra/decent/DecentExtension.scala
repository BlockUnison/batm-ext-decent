package com.generalbytes.batm.server.extensions.extra.decent

import cats._
import cats.implicits._
import com.generalbytes.batm.common.Alias.{Attempt, Task}
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common.ratesources.SingleFixedPriceRateSource
import com.generalbytes.batm.common.{CryptoCurrency, Currency, Extension, Wallet, _}
import com.generalbytes.batm.server.extensions.{IExchange, IExchangeAdvanced, IRateSourceAdvanced}
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex.BittrexXchange
import org.http4s.Uri

class DecentExtension extends Extension[Currency.DCT] {
  override val name: String = "DCT Extension"
  override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent)

  private val loginData = """dctd:(https?):([A-Za-z0-9]+):([A-Za-z0-9\.]+):([A-Za-z0-9.]+)""".r

  override def createWallet(loginInfo: String): Attempt[Wallet[Currency.DCT, Task]] = loginInfo match {
    case loginData(protocol, user, password, hostname) => {
      val url = Uri.unsafeFromString(s"$protocol://$hostname")
      new DecentWalletRestApi(url, DecentWalletCredentials(user, password)) |> Right.apply
    }
    case _ => "Login info did not match the expected format" |> Left.apply
  }

  override def createRateSource: Attempt[IRateSourceAdvanced] = new XChangeAdapter(new BittrexXchange(Currency.Euro)).asRight[String]

  override def createExchange(loginInfo: String): Attempt[IExchangeAdvanced] = {
    new XChangeAdapter(new BittrexXchange(Currency.Euro)).asRight[String]
  }
}


