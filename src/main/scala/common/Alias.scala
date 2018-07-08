package common

import cats.effect.IO

object Alias {
  type Identifier = String
  type Address = String
  type Amount = Long
  type Error = String
  type BlockNumber = Long
  type Task[A] = IO[Attempt[A]]
  type Attempt[A] = Either[Error, A]
}
