package com.generalbytes.batm.common

case class CurrencyPair[From <: Currency, To <: Currency](from: From, to: To)