package com.simplaex.dummies.util;

import java.util.function.Supplier;

final public class Lazy<T> implements Supplier<T> {

  private Supplier<T> supplier;
  private T thing = null;

  Lazy(final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  @Override
  public T get() {
    if (supplier == null) {
      return thing;
    }
    synchronized (this) {
      if (thing == null) {
        try {
          thing = supplier.get();
        } finally {
          this.supplier = null;
        }
      }
    }
    return thing;
  }
}
