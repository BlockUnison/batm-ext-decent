package org.knowm.xchange.internal.service.trade.params;

public interface TradeHistoryParamNextPageCursor extends TradeHistoryParams {
  String getNextPageCursor();

  void setNextPageCursor(String cursor);
}
