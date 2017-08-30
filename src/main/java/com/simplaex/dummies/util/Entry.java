package com.simplaex.dummies.util;

import lombok.Value;

import java.util.Map;

@Value
public class Entry<K, V> implements Map.Entry<K, V> {

  private K key;

  private V value;

  @Override
  public V setValue(final V value) {
    throw new UnsupportedOperationException();
  }
}
