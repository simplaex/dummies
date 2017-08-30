package com.simplaex.dummies;

import lombok.Value;

import java.util.Iterator;
import java.util.LinkedHashMap;

@Value
final class ClassInfo implements Iterable<PropertyInfo<?>> {

  private final Class<?> type;

  private final LinkedHashMap<String, PropertyInfo<?>> properties;

  private final LinkedHashMap<String, FieldInfo<?>> fields;

  @Override
  public Iterator<PropertyInfo<?>> iterator() {
    return properties.values().iterator();
  }
}
