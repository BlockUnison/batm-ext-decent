package common

sealed trait Currency {
  val name: String
}

sealed trait Fiat { self: Currency => }
sealed trait Crypto { self: Currency => }

trait FiatCurrency extends Currency with Fiat
trait CryptoCurrency extends Currency with Crypto

object Currency {
  def apply[T <: Currency : Default]: T = implicitly[Default[T]].value
  trait DCT extends CryptoCurrency { val name = "DCT" }
  trait BTC extends CryptoCurrency { val name = "BTC" }

  case object Decent extends DCT
  case object Bitcoin extends BTC

  trait EUR extends FiatCurrency { val name = "EUR" }

  trait Default[T <: Currency] {
    val value: T
  }

  implicit val dct: Default[DCT] = new Default[DCT] { val value = Decent }
  implicit val btc: Default[BTC] = new Default[BTC] { val value = Bitcoin }
}