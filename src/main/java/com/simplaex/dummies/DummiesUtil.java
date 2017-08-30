package com.simplaex.dummies;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@UtilityClass
class DummiesUtil {

  static <A> Lazy<A> lazy(final Supplier<A> supplier) {
    return new Lazy<>(supplier);
  }

  static <A> Constructor<A> getInjectableConstructor(final Class<A> clazz) {

    @SuppressWarnings("unchecked") List<Constructor<A>> constructors =
        Arrays.asList((Constructor<A>[]) clazz.getConstructors());

    if (constructors.size() != 1) {
      constructors = constructors.stream()
          .filter(constructor -> Arrays.stream(constructor.getDeclaredAnnotations())
              .anyMatch(annotation -> {
                val annotationType = annotation.annotationType();
                return annotationType.getPackage().getName().equals("javax.inject")
                    && annotationType.getSimpleName().equals("Inject");
              }))
          .collect(Collectors.toList());
    }
    if (constructors.size() != 1) {
      try {
        constructors = Collections.singletonList(clazz.getConstructor());
      } catch (final NoSuchMethodException ignore) {
      }
    }
    if (constructors.size() != 1) {
      return null;
    }
    return constructors.get(0);
  }

  static <A> Supplier<A> constructorToSupplier(final Constructor<A> constructor) {
    return () -> {
      try {
        return constructor.newInstance();
      } catch (final Exception exc) {
        return null;
      }
    };
  }
}
