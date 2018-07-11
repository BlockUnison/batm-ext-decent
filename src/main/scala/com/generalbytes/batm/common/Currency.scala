package com.generalbytes.batm.common

import Alias.Attempt
import shapeless._
import syntax.typeable._

sealed trait Currency {
  val name: String
}

sealed trait Fiat { self: Currency => }
sealed trait Crypto { self: Currency => }

trait FiatCurrency extends Currency with Fiat
trait CryptoCurrency extends Currency with Crypto

object Currency {
//  def withName[T <: Currency](currency: String): Attempt[T] = {
//    allMap.get(currency).flatMap(_.cast[T]).toRight("Currency not found")
//  }

  def apply[T <: Currency : Default]: T = implicitly[Default[T]].value

  trait DCT extends CryptoCurrency { val name = "DCT" }
  trait BTC extends CryptoCurrency { val name = "BTC" }
  case object Decent extends DCT
  case object Bitcoin extends BTC

  trait EUR extends FiatCurrency { val name = "EUR" }
  case object Euro extends EUR

  trait Default[T <: Currency] {
    val value: T
  }

  val all: Set[Currency] = Set(Decent, Bitcoin, Euro)
  val allMap: Map[String, Currency] = all.map(c => c.name -> c).toMap

  implicit val dct: Default[DCT] = new Default[DCT] { val value = Decent }
  implicit val btc: Default[BTC] = new Default[BTC] { val value = Bitcoin }
}