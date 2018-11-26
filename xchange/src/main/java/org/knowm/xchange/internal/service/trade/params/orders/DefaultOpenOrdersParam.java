package org.knowm.xchange.internal.service.trade.params.orders;

import org.knowm.xchange.internal.dto.trade.LimitOrder;

public class DefaultOpenOrdersParam implements OpenOrdersParams {

  public DefaultOpenOrdersParam() {}

  @Override
  public boolean accept(LimitOrder order) {
    return order != null;
  }
}
