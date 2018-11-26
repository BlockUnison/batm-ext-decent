package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.Currency;

public interface TradeHistoryParamCurrencies extends TradeHistoryParams {

  Currency[] getCurrencies();

  void setCurrencies(Currency[] currencies);
}
