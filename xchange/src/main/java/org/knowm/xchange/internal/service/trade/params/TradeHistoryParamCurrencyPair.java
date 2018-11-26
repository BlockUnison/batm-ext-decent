package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.CurrencyPair;

public interface TradeHistoryParamCurrencyPair extends TradeHistoryParams {

  CurrencyPair getCurrencyPair();

  void setCurrencyPair(CurrencyPair pair);
}
