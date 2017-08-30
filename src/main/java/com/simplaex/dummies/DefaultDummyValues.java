package com.simplaex.dummies;

import com.simplaex.dummies.annotation.DummyValues;

@DummyValues(
    minLength = 0,
    maxLength = 5
)
final class DefaultDummyValues {

  private static final DummyValues instance =
      DefaultDummyValues.class.getAnnotation(DummyValues.class);

  public static DummyValues get() {
    return instance;
  }

}
