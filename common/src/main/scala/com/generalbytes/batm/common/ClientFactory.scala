package com.generalbytes.batm.common

import cats.implicits._
import cats.effect._
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import implicits._

trait ClientFactory[F[_]] {

  def client(implicit F: ConcurrentEffect[F]): F[Client[F]] = {
    def liftOpt[A](a: Option[A]): F[A] = a.fold(F.raiseError[A](err"Could not construct client"))(F.pure[A])

    BlazeClientBuilder[F](scala.concurrent.ExecutionContext.global)
      .stream.compile.last.flatMap(liftOpt)
  }
}
