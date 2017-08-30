package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;

public class NameGenerator implements Generator<String> {

  private final FirstNameGenerator firstNameGenerator;
  private final LastNameGenerator lastNameGenerator;
  private final Dummies dummies;

  public NameGenerator(final Dummies dummies) {
    this.dummies = dummies;
    this.firstNameGenerator = new FirstNameGenerator(dummies);
    this.lastNameGenerator = new LastNameGenerator(dummies);
  }

  @Override
  public String get() {
    return firstNameGenerator.get() + " " + lastNameGenerator.get();
  }
}
