package com.generalbytes.batm.common

import com.typesafe.scalalogging.Logger
import Alias.Attempt

import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.{higherKinds, postfixOps}

object Util {
  implicit class Pipe[A](a: => A) {
    def |>[B](f: A => B): B = f(a)
  }

  implicit class AttemptOps[A](a: Attempt[A]) {
    def getOrThrow: A = {
      a.fold(e => throw new Exception(e), identity)
    }

    def logError(implicit logger: Logger): Attempt[A] = {
      log(a)
    }
  }

  implicit class EitherOps[A](self: Either[A, A]) {
    def value: A = self.fold(identity, identity)
  }

  implicit val defaultDuration: Duration = 5 seconds

  def log[A](self: Attempt[A])(implicit logger: Logger): Attempt[A] = {
    self.left.foreach(x => logger.error(x))
    self
  }

  implicit class SetExtensions[A](self: Set[A]) {
    def toJavaSet: java.util.Set[A] = mutable.Set.apply(self.toList: _*).asJava
  }

  def hmacsha256(message: String, secretKey: String): String = {
    val secret = new SecretKeySpec(secretKey.getBytes, "SHA256")   //Crypto Funs : 'SHA256' , 'HmacSHA1'
    val mac = Mac.getInstance("SHA256")
    mac.init(secret)
    val hashString: Array[Byte] = mac.doFinal(message.getBytes)
    new String(hashString.map(_.toChar))
  }
}
