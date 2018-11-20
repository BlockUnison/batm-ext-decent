package com.generalbytes.batm.server.extensions.extra.bitcoincash

import com.generalbytes.batm.common.adapters.ExtensionAdapter
import com.generalbytes.batm.common.domain.{CryptoCurrency, Currency, Extension, Task}
import com.generalbytes.batm.common.implicits._
import com.generalbytes.batm.common.factories.{DummyAddressValidatorFactory, DummyWalletFactory}
import com.generalbytes.batm.server.extensions.extra.coinmate.{CoinmateExchangeFactory, CoinmateRateSourceFactory}

class BitcoinCashExtension extends ExtensionAdapter[Task](new BitcoinCashExtension.Impl)

object BitcoinCashExtension {
  class Impl extends Extension
  with CoinmateExchangeFactory
  with CoinmateRateSourceFactory
  with DummyWalletFactory
  with DummyAddressValidatorFactory {

    override val name: String = "Coinmate extension"
    override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(Currency.BitcoinCash, Currency.Bitcoin)
  }
}