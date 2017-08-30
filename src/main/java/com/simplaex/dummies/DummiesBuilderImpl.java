package com.simplaex.dummies;

import com.simplaex.dummies.util.Cons;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.simplaex.dummies.util.UtilityBelt.cons;
import static com.simplaex.dummies.util.UtilityBelt.entry;

final class DummiesBuilderImpl implements Dummies.Builder {

  private final Long seed;

  private final boolean secure;

  private final Consumer<Exception> exceptionHandler;

  private final Cons<Map.Entry<Class<?>, Generator<?>>> generators;

  private final Cons<Map.Entry<Class<?>, Class<? extends Generator<?>>>> generatorTypes;

  DummiesBuilderImpl() {
    this(
        null,
        false,
        DummiesSingleton.noOpExceptionHandler,
        Cons.empty(),
        Cons.empty()
    );
  }

  private DummiesBuilderImpl(
      final Long seed,
      final boolean secure,
      final Consumer<Exception> exceptionHandler,
      final Cons<Map.Entry<Class<?>, Generator<?>>> generators,
      final Cons<Map.Entry<Class<?>, Class<? extends Generator<?>>>> generatorTypes
  ) {
    this.seed = seed;
    this.secure = secure;
    this.exceptionHandler = exceptionHandler;
    this.generators = generators;
    this.generatorTypes = generatorTypes;
  }

  @Override
  public DummiesBuilderImpl secure() {
    return new DummiesBuilderImpl(seed, true, exceptionHandler, generators, generatorTypes);
  }

  @Override
  public DummiesBuilderImpl withSeed(final long seed) {
    return new DummiesBuilderImpl(seed, secure, exceptionHandler, generators, generatorTypes);
  }

  @Override
  public <T> DummiesBuilderImpl withGenerator(final Class<T> clazz, final Generator<T> generator) {
    return new DummiesBuilderImpl(seed, secure, exceptionHandler, cons(entry(clazz, generator), generators), generatorTypes);
  }

  @Override
  public <T, G extends Generator<T>> DummiesBuilderImpl withGenerator(final Class<T> clazz, final Class<G> generatorType) {
    return new DummiesBuilderImpl(seed, secure, exceptionHandler, generators, cons(entry(clazz, generatorType), generatorTypes));
  }

  @Override
  public Dummies build() {
    final Supplier<Random> randomSupplier;
    if (secure) {
      final Random random = new SecureRandom();
      randomSupplier = () -> random;
    } else if (seed != null) {
      final Random random = new Random(seed);
      randomSupplier = () -> random;
    } else {
      randomSupplier = ThreadLocalRandom::current;
    }
    final DummiesImpl instance = new DummiesImpl(
        randomSupplier,
        exceptionHandler
    );
    if (generators != null) {
      for (final Map.Entry<Class<?>, Generator<?>> entry : generators) {
        instance.addGenerator(entry.getKey(), entry.getValue());
      }
    }
    if (generatorTypes != null) {
      for (final Map.Entry<Class<?>, Class<? extends Generator<?>>> entry : generatorTypes) {
        instance.addGenerator(entry.getKey(), entry.getValue());
      }
    }
    return instance;
  }

}