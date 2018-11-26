package org.knowm.xchange.internal.bittrex;

import org.apache.commons.lang3.StringUtils;
import org.knowm.xchange.internal.bittrex.dto.BittrexException;
import org.knowm.xchange.internal.exceptions.CurrencyPairNotValidException;
import org.knowm.xchange.internal.exceptions.ExchangeException;

/** @author walec51 */
public class BittrexErrorAdapter {

  public static ExchangeException adapt(BittrexException e) {
    String message = e.getMessage();
    if (StringUtils.isEmpty(message)) {
      return new ExchangeException("Operation failed without any error message");
    }
    switch (message) {
      case "INVALID_MARKET":
        return new CurrencyPairNotValidException();
    }
    return new ExchangeException(message);
  }
}
