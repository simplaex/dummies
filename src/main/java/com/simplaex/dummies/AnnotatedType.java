package com.simplaex.dummies;

import lombok.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

@Value
class AnnotatedType {

  private String name;

  private TypeInfo typeInfo;

  private Map<Class<? extends Annotation>, Annotation> annotationMap;

  <A extends Annotation> Optional<A> getAnnotation(final Class<A> annotationType) {
    final Annotation annotation = annotationMap.get(annotationType);
    if (annotation == null || !annotation.annotationType().equals(annotationType)) {
      return Optional.empty();
    }
    //noinspection unchecked
    return Optional.of((A) annotation);
  }

}
