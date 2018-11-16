package com.generalbytes.batm.server.extensions.extra.decent.extension

import cats.implicits._
import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.factories.ExchangeFactory
import com.generalbytes.batm.server.extensions.IExchange
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.btrx.{CounterReplacingXChangeWrapper, DefaultBittrexXChangeWrapper}

case class LoginInfo(apiKey: String, secretKey: String)

trait BittrexExchangeFactory extends ExchangeFactory {
  import Currency._

  private val exchangeLoginData = """btrx:([A-Za-z0-9]+):([A-Za-z0-9]+)""".r

  protected def parseExchangeLoginInfo(loginInfo: String): Option[LoginInfo] = loginInfo match {
    case exchangeLoginData(apiKey, secretKey) => LoginInfo(apiKey, secretKey).some
    case _ => none
  }

  def createExchange(loginInfo: String): Attempt[IExchange] = {
    parseExchangeLoginInfo(loginInfo)
      .map(creds => new ExchangeAdapter[Task](
        new CounterReplacingXChangeWrapper[Task](
          new DefaultBittrexXChangeWrapper[Task](creds),
          List(CurrencyPair(Euro, Bitcoin), CurrencyPair(USDollar, Bitcoin)),
          List(USDollar)
        )
      ))
      .toRight(err"Could not create exchange from params: $loginInfo")
  }
}
