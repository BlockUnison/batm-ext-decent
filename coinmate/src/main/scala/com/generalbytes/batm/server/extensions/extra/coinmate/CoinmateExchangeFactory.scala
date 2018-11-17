package com.generalbytes.batm.server.extensions.extra.coinmate

import cats.implicits._
import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.domain.{Attempt, Task}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.utils.Util._
import com.generalbytes.batm.common.factories.ExchangeFactory
import com.generalbytes.batm.server.extensions.IExchange

case class CoinmateLoginInfo(clientId: String, publicKey: String, privateKey: String)

trait CoinmateExchangeFactory extends ExchangeFactory with CoinmateLoginInfoFactory {

  def createExchange(loginInfo: String): Attempt[IExchange] =
    parseLoginInfo(loginInfo).map { creds =>
      new ExchangeAdapter(
        new CoinmateXchangeWrapper[Task](creds)
      )
    }.toRight(err"Could not create exchange from params: $loginInfo")
}
