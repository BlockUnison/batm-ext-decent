package org.knowm.xchange.internal.service.marketdata.params;

import java.util.Collection;
import org.knowm.xchange.internal.currency.CurrencyPair;

public interface CurrencyPairsParam extends Params {

  Collection<CurrencyPair> getCurrencyPairs();
}
