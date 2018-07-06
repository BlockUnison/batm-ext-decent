package common

import scala.concurrent.Future

object Alias {
  type Identifier = String
  type Address = String
  type Amount = Long
  type Error = String
  type BlockNumber = Long
  type Task[A] = Future[A]
  type Attempt[A] = Either[Error, A]
}
