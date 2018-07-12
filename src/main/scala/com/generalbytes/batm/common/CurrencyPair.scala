package com.generalbytes.batm.common

trait CurrencyPair[From <: Currency, To <: Currency] {
  val from: From
  val to: To
}

object CurrencyPair {
  def apply[F <: Currency, T <: Currency](from: F, to: T): CurrencyPair[F, T] = new CurrencyPair[F, T] {
    val from: F = from
    val to: T = to
  }
}