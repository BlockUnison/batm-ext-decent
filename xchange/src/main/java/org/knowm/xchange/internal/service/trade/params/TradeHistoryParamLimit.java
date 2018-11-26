package org.knowm.xchange.internal.service.trade.params;

public interface TradeHistoryParamLimit extends TradeHistoryParams {

  Integer getLimit();

  void setLimit(Integer limit);
}
