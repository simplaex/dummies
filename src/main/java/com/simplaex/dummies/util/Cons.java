package com.simplaex.dummies.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Iterator;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Cons<T> implements Iterable<T> {

  private static final Cons<?> EMPTY = new Cons<>(null, null);

  @SuppressWarnings("unchecked")
  public static <T> Cons<T> empty() {
    return (Cons<T>) EMPTY;
  }

  final T head;
  
  final Cons<T> tail;

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {

      private Cons<T> current = Cons.this;

      @Override
      public boolean hasNext() {
        return current != EMPTY;
      }

      @Override
      public T next() {
        final T head = current.head;
        current = current.tail;
        return head;
      }
    };
  }
}