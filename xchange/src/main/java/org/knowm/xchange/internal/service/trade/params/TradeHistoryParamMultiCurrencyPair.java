package org.knowm.xchange.internal.service.trade.params;

import java.util.Collection;
import org.knowm.xchange.internal.currency.CurrencyPair;

public interface TradeHistoryParamMultiCurrencyPair extends TradeHistoryParams {

  Collection<CurrencyPair> getCurrencyPairs();

  void setCurrencyPairs(Collection<CurrencyPair> pairs);
}
