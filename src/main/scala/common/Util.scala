package common

import scala.concurrent.duration._
import scala.language.postfixOps

object Util {
  implicit class Pipe[A](a: => A) {
    def |>[B](f: A => B): B = f(a)
  }

  implicit val defaultDuration: Duration = 5 seconds
}
