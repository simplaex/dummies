package com.simplaex.dummies;

import java.util.function.Supplier;

@FunctionalInterface
public interface Generator<T> extends Supplier<T> {

  class NullGenerator implements Generator<Object> {
    @Override
    public Object get() {
      return null;
    }
  }

  @Override
  T get();

}
