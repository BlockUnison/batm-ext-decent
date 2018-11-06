package com.generalbytes.batm.common.exchanges

import cats.effect.Sync
import com.generalbytes.batm.common.Alias.Identifier
import com.generalbytes.batm.common.Util._
import com.generalbytes.batm.common._
import com.generalbytes.batm.common.adapters.ExchangeAdapterDecorator
import retry._

class RetryingExchangeWrapper[F[_] : Sleep : Sync](exchange: Exchange[F], maxRetries: Int)
  extends ExchangeAdapterDecorator[F](exchange) with LoggingSupport {

  override def fulfillOrder(order: TradeOrder): F[Identifier] =
    retryingOnAllErrors(
      RetryPolicies.limitRetries(maxRetries), logOp[F, Throwable]
    ) {
      exchange.fulfillOrder(order)
    }
}
