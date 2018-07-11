package com.generalbytes.batm.server.common

trait Order[T <: Currency] {

}

object Order {
  def create[T <: Currency](currency: T): Order[T] = new Order[T] {}
}