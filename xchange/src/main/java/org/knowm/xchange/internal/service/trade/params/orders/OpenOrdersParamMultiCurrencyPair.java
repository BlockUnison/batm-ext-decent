package org.knowm.xchange.internal.service.trade.params.orders;

import java.util.Collection;
import org.knowm.xchange.internal.currency.CurrencyPair;
import org.knowm.xchange.internal.dto.trade.LimitOrder;

public interface OpenOrdersParamMultiCurrencyPair extends OpenOrdersParams {
  @Override
  default boolean accept(LimitOrder order) {
    return order != null
        && getCurrencyPairs() != null
        && getCurrencyPairs().contains(order.getCurrencyPair());
  }

  Collection<CurrencyPair> getCurrencyPairs();

  void setCurrencyPairs(Collection<CurrencyPair> pairs);
}
