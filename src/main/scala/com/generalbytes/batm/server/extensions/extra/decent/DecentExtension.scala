package com.generalbytes.batm.server.extensions.extra.decent

import com.generalbytes.batm.server.common.Alias.{Attempt, Task}
import com.generalbytes.batm.server.common.{CryptoCurrency, Currency, Extension, Wallet}
import com.generalbytes.batm.server.common._
import com.generalbytes.batm.server.common.Util._
import org.http4s.Uri

class DecentExtension extends Extension[Currency.DCT] {
  override val name: String = "DCT Extension"
  override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(new Currency.DCT {})

  private val loginData = """dctd:(https?):([A-Za-z0-9]+):([A-Za-z0-9\.]+):([A-Za-z0-9.]+)""".r

  override def createWallet(loginInfo: String): Attempt[Wallet[Currency.DCT, Task]] = loginInfo match {
    case loginData(protocol, user, password, hostname) => {
      val url = Uri.unsafeFromString(s"$protocol://$hostname")
      new DecentWalletRestApi(url, DecentWalletCredentials(user, password)) |> Right.apply
    }
    case _ => "Login info did not match the expected format" |> Left.apply
  }
}


