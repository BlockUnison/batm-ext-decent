package common

import common.Alias.Attempt

import scala.concurrent.duration._
import scala.language.postfixOps

object Util {
  implicit class Pipe[A](a: => A) {
    def |>[B](f: A => B): B = f(a)
  }

  implicit class AttemptOps[A](a: Attempt[A]) {
    def getOrThrow: A = {
      a.fold(e => throw new Exception(e), identity)
    }
  }

  implicit val defaultDuration: Duration = 5 seconds
}
