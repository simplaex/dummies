package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Locale;

@RequiredArgsConstructor
public class CountryCodeGenerator implements Generator<String> {

  private final Dummies dummies;

  private static final String[] countryCodes = Locale.getISOCountries();

  static {
    for (int i = 0; i < countryCodes.length; i += 1) {
      val locale = new Locale("", countryCodes[i]);
      countryCodes[i] = locale.getCountry();
    }
  }

  @Override
  public String get() {
    return countryCodes[dummies.getRandom().nextInt(countryCodes.length)];
  }

}
