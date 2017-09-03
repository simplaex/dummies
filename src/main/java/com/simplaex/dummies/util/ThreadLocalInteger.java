package com.simplaex.dummies.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.IntUnaryOperator;

public class ThreadLocalInteger {

  @Data
  @AllArgsConstructor
  private static class MutableInteger {
    private int value = 0;
  }

  private final ThreadLocal<MutableInteger> integer;

  public ThreadLocalInteger() {
    this(0);
  }

  public ThreadLocalInteger(final int initivalValue) {
    integer = ThreadLocal.withInitial(() -> new MutableInteger(initivalValue));
  }

  public int get() {
    return integer.get().getValue();
  }

  public void set(final int newValue) {
    integer.get().setValue(newValue);
  }

  public void update(final IntUnaryOperator updater) {
    final MutableInteger mutableInteger = integer.get();
    mutableInteger.setValue(updater.applyAsInt(mutableInteger.getValue()));
  }

}
