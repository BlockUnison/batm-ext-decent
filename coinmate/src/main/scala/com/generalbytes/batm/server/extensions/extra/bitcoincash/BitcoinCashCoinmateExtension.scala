package com.generalbytes.batm.server.extensions.extra.bitcoincash

import com.generalbytes.batm.common.adapters.ExtensionAdapter
import com.generalbytes.batm.common.domain.{CryptoCurrency, Currency, Extension}
import com.generalbytes.batm.common.factories.{DummyAddressValidatorFactory, DummyWalletFactory}
import com.generalbytes.batm.server.extensions.extra.coinmate.{CoinmateExchangeFactory, CoinmateRateSourceFactory}

abstract class BitcoinCashCoinmateExtension extends ExtensionAdapter(new BitcoinCashCoinmateExtension.Impl)

object BitcoinCashCoinmateExtension {
  class Impl extends Extension
  with CoinmateExchangeFactory
  with CoinmateRateSourceFactory
  with DummyWalletFactory
  with DummyAddressValidatorFactory {

    override val name: String = "Coinmate extension (ext)"
    override val supportedCryptoCurrencies: Set[CryptoCurrency] = Set(Currency.BitcoinCash, Currency.Bitcoin)
  }
}