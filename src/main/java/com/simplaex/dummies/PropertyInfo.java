package com.simplaex.dummies;

import lombok.Value;

import java.lang.reflect.Method;

@Value
final class PropertyInfo<T> {

  private AnnotatedType annotatedType;

  private Generator<T> generator;

  private Method writeMethod;

  static <V> PropertyInfo<V> create(
      final AnnotatedType annotatedType,
      final Generator<V> generator,
      final Method writeMethod
  ) {
    return new PropertyInfo<>(annotatedType, generator, writeMethod);
  }
}
