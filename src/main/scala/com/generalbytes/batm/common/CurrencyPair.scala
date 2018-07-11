package com.generalbytes.batm.common

trait CurrencyPair[From <: Currency, To <: Currency] {
  val from: From
  val to: To
}
