package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ThreeLetterCountryCodeGenerator implements Generator<String> {

  private final Dummies dummies;

  private static final String[] countryCodes = Locale.getISOCountries();

  static {
    for (int i = 0; i < countryCodes.length; i += 1) {
      val locale = new Locale("", countryCodes[i]);
      countryCodes[i] = locale.getISO3Country();
    }
  }

  @Override
  public String get() {
    return countryCodes[dummies.getRandom().nextInt(countryCodes.length)];
  }

}
