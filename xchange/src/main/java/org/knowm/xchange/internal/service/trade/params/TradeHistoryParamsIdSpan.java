package org.knowm.xchange.internal.service.trade.params;

public interface TradeHistoryParamsIdSpan extends TradeHistoryParams {

  String getStartId();

  void setStartId(String startId);

  String getEndId();

  void setEndId(String endId);
}
