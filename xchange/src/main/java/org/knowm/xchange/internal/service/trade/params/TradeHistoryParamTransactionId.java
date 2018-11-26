package org.knowm.xchange.internal.service.trade.params;

public interface TradeHistoryParamTransactionId extends TradeHistoryParams {
  String getTransactionId();

  void setTransactionId(String txId);
}
