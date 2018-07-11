package com.generalbytes.batm.common

trait Order[T <: Currency] {

}

object Order {
  def create[T <: Currency](currency: T): Order[T] = new Order[T] {}
}