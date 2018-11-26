package org.knowm.xchange.internal.bittrex.service;

import org.knowm.xchange.internal.Exchange;
import org.knowm.xchange.internal.bittrex.BittrexAuthenticated;
import org.knowm.xchange.internal.bittrex.BittrexV2;
import org.knowm.xchange.internal.service.BaseService;
import org.knowm.xchange.internal.service.BaseExchangeService;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

public class BittrexBaseService extends BaseExchangeService implements BaseService {

  protected final String apiKey;
  protected final BittrexAuthenticated bittrexAuthenticated;
  protected final BittrexV2 bittrexV2;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  public BittrexBaseService(Exchange exchange) {

    super(exchange);

    this.bittrexAuthenticated =
        RestProxyFactory.createProxy(
            BittrexAuthenticated.class,
            exchange.getExchangeSpecification().getSslUri(),
            getClientConfig());
    this.bittrexV2 =
        RestProxyFactory.createProxy(
            BittrexV2.class, exchange.getExchangeSpecification().getSslUri(), getClientConfig());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator =
        BittrexDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }
}
