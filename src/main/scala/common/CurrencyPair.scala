package common

trait CurrencyPair[From <: Currency, To <: Currency] {
  val from: From
  val to: To
}
