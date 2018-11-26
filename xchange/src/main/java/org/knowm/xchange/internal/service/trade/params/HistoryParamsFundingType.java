package org.knowm.xchange.internal.service.trade.params;

import org.knowm.xchange.internal.dto.account.FundingRecord;

public interface HistoryParamsFundingType extends TradeHistoryParams {

  FundingRecord.Type getType();

  void setType(FundingRecord.Type type);
}
