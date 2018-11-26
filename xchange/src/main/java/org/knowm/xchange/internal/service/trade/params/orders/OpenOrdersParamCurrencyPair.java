package org.knowm.xchange.internal.service.trade.params.orders;

import org.knowm.xchange.internal.currency.CurrencyPair;
import org.knowm.xchange.internal.dto.trade.LimitOrder;

public interface OpenOrdersParamCurrencyPair extends OpenOrdersParams {
  @Override
  default boolean accept(LimitOrder order) {
    return order != null
        && getCurrencyPair() != null
        && getCurrencyPair().equals(order.getCurrencyPair());
  }

  CurrencyPair getCurrencyPair();

  void setCurrencyPair(CurrencyPair pair);
}
