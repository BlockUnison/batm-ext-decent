package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.CurrencyPair;

public interface CancelOrderByCurrencyPair extends CancelOrderParams {

  public CurrencyPair getCurrencyPair();
}
