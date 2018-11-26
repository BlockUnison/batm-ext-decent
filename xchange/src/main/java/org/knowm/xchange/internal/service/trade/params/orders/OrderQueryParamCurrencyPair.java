package org.knowm.xchange.internal.service.trade.params.orders;

import org.knowm.xchange.internal.currency.CurrencyPair;

public interface OrderQueryParamCurrencyPair extends OrderQueryParams {
  CurrencyPair getCurrencyPair();

  void setCurrencyPair(CurrencyPair currencyPair);
}
