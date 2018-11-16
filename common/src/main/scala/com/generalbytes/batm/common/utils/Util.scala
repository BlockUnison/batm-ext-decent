package com.generalbytes.batm.common

import cats.effect.Sync
import cats.implicits._
import com.generalbytes.batm.common.Alias.{ApplicativeErr, Attempt}
import org.slf4j.Logger
import retry.RetryDetails

import scala.language.higherKinds

object Util {

  def logObj[A](self: Attempt[A])(implicit logger: Logger): Attempt[A] = {
    self.left.foreach(x => logger.error(x.toString))
    self.right.foreach(x => logger.debug(s"Value: $x"))
    self
  }

  class LogPartiallyApplied[F[_]] {
    def apply[A](a: A, message: String = "Value")(implicit F: Sync[F], logger: Logger): F[A] = F.delay {
      logger.debug(s"$message: $a")
      a
    }
  }

  def log[F[_]] = new LogPartiallyApplied[F]

  class RaisePartiallyApplied[F[_]] {
    def apply[A](e: Throwable)(implicit F: ApplicativeErr[F]): F[A] = F.raiseError(e)
  }

  def raise[F[_]] = new RaisePartiallyApplied[F]
  def delay[F[_]: Sync, A](thunk: => A): F[A] = Sync[F].delay(thunk)

  def logOp[M[_], A](implicit M: Sync[M], logger: Logger): (A, RetryDetails) => M[Unit] =
    (a, _) => log[M](a).void

  private val hmacSHA256 = "HmacSHA256"
  private val hmacSHA512 = "HmacSHA512"

  def hmacsha256(message: String, secretKey: String): String = hmacsha(message, secretKey, hmacSHA256)
  def hmacsha512(message: String, secretKey: String): String = hmacsha(message, secretKey, hmacSHA512)

  private def hmacsha(message: String, secretKey: String, algorithm: String): String = {
    import javax.crypto.Mac
    import javax.crypto.spec.SecretKeySpec
    val sha256_HMAC = Mac.getInstance(algorithm)
    val secret_key = new SecretKeySpec(secretKey.getBytes, hmacSHA256)
    sha256_HMAC.init(secret_key)

    Hex.valueOf(sha256_HMAC.doFinal(message.getBytes))
  }
}

object Hex {
  def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString
}