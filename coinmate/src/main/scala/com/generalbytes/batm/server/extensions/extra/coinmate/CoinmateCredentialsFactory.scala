package com.generalbytes.batm.server.extensions.extra.coinmate

import cats.implicits._

trait CoinmateCredentialsFactory {
  private val exchangeLoginData = """coinmate:([0-9]+):([A-Za-z0-9_]+):([A-Za-z0-9_]+)""".r

  def parseLoginInfo(loginInfo: String): Option[CoinmateLoginInfo] = loginInfo match {
    case exchangeLoginData(clientId, publicKey, privateKey) =>
      CoinmateLoginInfo(clientId, publicKey, privateKey).some
    case _ => none
  }
}
