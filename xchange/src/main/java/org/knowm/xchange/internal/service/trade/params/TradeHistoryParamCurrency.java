package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.Currency;

public interface TradeHistoryParamCurrency extends TradeHistoryParams {

  Currency getCurrency();

  void setCurrency(Currency currency);
}
