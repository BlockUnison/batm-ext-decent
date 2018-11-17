package com.generalbytes.batm.server.extensions.extra.decent.extension

import com.generalbytes.batm.common.domain.{CryptoCurrency, Currency, Extension}
import com.generalbytes.batm.common.factories.DummyAddressValidatorFactory

class DecentExtension extends Extension
  with CompositeRateSourceFactory
  with BittrexExchangeFactory
  with DecentHotWalletFactory
  with DummyAddressValidatorFactory {

  override val name: String = "DCT Extension"
  override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(Currency.Decent, Currency.Bitcoin)
}


