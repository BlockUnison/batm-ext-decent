package com.generalbytes.batm.common

import com.generalbytes.batm.common.Alias.Attempt
import com.typesafe.scalalogging.Logger

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

  implicit class OptionOps[A](a: Option[A]) {
    def getOrThrow(message: String): A = {
      a.fold(throw new Exception(message))(identity)
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
    import javax.crypto.Mac
    import javax.crypto.spec.SecretKeySpec
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    val secret_key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256")
    sha256_HMAC.init(secret_key)

    Hex.valueOf(sha256_HMAC.doFinal(message.getBytes("UTF-8")))
  }
}

object Hex {
  def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString
}
