package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.CurrencyPair;

public interface CancelOrderByPairAndIdParams extends CancelOrderByIdParams {
  CurrencyPair getCurrencyPair();
}
