package com.generalbytes.batm.server.extensions.extra.decent.factories

import cats.implicits._
import com.generalbytes.batm.common.domain._
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.adapters.ExchangeAdapter
import com.generalbytes.batm.common.factories.ExchangeFactory
import com.generalbytes.batm.server.extensions.IExchange
import com.generalbytes.batm.server.extensions.extra.decent.exchanges.bittrex.{CounterReplacingXChangeWrapper, DefaultBittrexXChangeWrapper}

case class Credentials(apiKey: String, secretKey: String)
case class ExchangeParams(replacements: List[CurrencyPair], intermediates: List[Currency], credentials: Credentials)

trait BittrexExchangeFactory extends ExchangeFactory {

  private val exchangeLoginData = """bittrex:([A-Za-z0-9]+):([A-Za-z0-9]+):([A-Z,->]+):([A-Z,]+)""".r

  private def getCurrencyPair(pair: String): Option[CurrencyPair] = {
    val list = Currency.parseCSV(pair.replace("->", ","))
    if (list.length != 2) none
    else CurrencyPair(list.head, list.tail.head).some
  }

  protected def parseExchangeLoginInfo(loginInfo: String): Option[ExchangeParams] = loginInfo match {
    case exchangeLoginData(apiKey, secretKey, replacements, intermediates) => ExchangeParams(
      replacements.split(',').toList.flatMap(s => getCurrencyPair(s).toList),
      Currency.parseCSV(intermediates),
      Credentials(apiKey, secretKey)).some
    case _ => none
  }

  def createExchange(loginInfo: String): Attempt[IExchange] = {
    parseExchangeLoginInfo(loginInfo)
      .map(params =>
        new ExchangeAdapter(
          new CounterReplacingXChangeWrapper(
            new DefaultBittrexXChangeWrapper[Task](params.credentials),
              params.replacements,
              params.intermediates
//              List(CurrencyPair(Euro, Bitcoin), CurrencyPair(USDollar, Bitcoin)),
//              List(USDollar)
          )
        )
      )
      .toRight(err"Could not create exchange from params: $loginInfo")
  }
}
