package com.generalbytes.batm.server.common

import com.typesafe.scalalogging.Logger
import Alias.Attempt

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
}
