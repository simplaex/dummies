package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Currency;
import java.util.Locale;

@RequiredArgsConstructor
public class CurrencyGenerator implements Generator<String> {

  private final Dummies dummies;

  private static final String[] currencyCodes = new String[Currency.getAvailableCurrencies().size()];

  static {
    val currencies = Currency.getAvailableCurrencies().toArray(new Currency[currencyCodes.length]);
    for (int i = 0; i < currencyCodes.length; i += 1) {
      currencyCodes[i] = currencies[i].getCurrencyCode();
    }
  }

  @Override
  public String get() {
    return currencyCodes[dummies.getRandom().nextInt(currencyCodes.length)];
  }

}
