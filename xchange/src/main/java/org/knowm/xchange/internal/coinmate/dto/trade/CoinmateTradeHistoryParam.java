package org.knowm.xchange.internal.coinmate.dto.trade;

import org.knowm.xchange.internal.currency.CurrencyPair;
import org.knowm.xchange.internal.service.trade.params.TradeHistoryParamCurrencyPair;

public class CoinmateTradeHistoryParam implements TradeHistoryParamCurrencyPair {

  CurrencyPair pair;
  int limit = 1000;

  public CoinmateTradeHistoryParam(CurrencyPair pair) {
    this.pair = pair;
  }

  public CoinmateTradeHistoryParam(CurrencyPair pair, int limit) {
    this.pair = pair;
    this.limit = limit;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return pair;
  }

  @Override
  public void setCurrencyPair(CurrencyPair pair) {
    this.pair = pair;
  }
}
