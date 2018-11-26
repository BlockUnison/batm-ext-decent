package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.dto.Order;

public interface CancelOrderByOrderTypeParams extends CancelOrderParams {
  Order.OrderType getOrderType();
}
