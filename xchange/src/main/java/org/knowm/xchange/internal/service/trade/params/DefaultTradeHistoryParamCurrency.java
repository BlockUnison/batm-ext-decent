package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.currency.Currency;

public class DefaultTradeHistoryParamCurrency implements TradeHistoryParamCurrency {

  private Currency currency;

  public DefaultTradeHistoryParamCurrency() {}

  public DefaultTradeHistoryParamCurrency(Currency currency) {
    this.currency = currency;
  }

  @Override
  public Currency getCurrency() {
    return this.currency;
  }

  @Override
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }
}
