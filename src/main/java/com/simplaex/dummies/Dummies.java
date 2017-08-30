package com.simplaex.dummies;

import java.util.Random;

public interface Dummies {

  <T> T create(final Class<T> clazz);

  <T> T fill(final T instance);

  <T> T fromString(final Class<T> clazz, final String stringValue);

  Random getRandom();


  static Dummies forSeed(final long seed) {
    return builder().withSeed(seed).build();
  }

  static Dummies secure() {
    return builder().secure().build();
  }

  static Dummies get() {
    return DummiesSingleton.INSTANCE;
  }

  static Builder builder() {
    return new DummiesBuilderImpl();
  }

  interface Builder {

    Builder secure();

    Builder withSeed(final long seed);

    <T> Builder withGenerator(final Class<T> clazz, final Generator<T> generator);

    <T, G extends Generator<T>> Builder withGenerator(final Class<T> clazz, final Class<G> generator);

    Dummies build();

  }
}
