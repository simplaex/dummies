package com.simplaex.dummies;

import lombok.Value;

@Value
class FieldInfo<T> {

  private AnnotatedType annotatedType;

  private Generator<T> generator;

  static <V> FieldInfo<V> create(
      final AnnotatedType annotatedType,
      final Generator<V> generator
  ) {
    return new FieldInfo<>(annotatedType, generator);
  }
}
