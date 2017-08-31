package com.simplaex.dummies.util;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@UtilityClass
public class UtilityBelt {

  public static <A> Lazy<A> lazy(final Supplier<A> supplier) {
    return new Lazy<>(supplier);
  }

  /**
   * Finds a constructor suitable for injection/creation of an instance.
   * <p>
   * If there is just one constructor then this one is returned.
   * <p>
   * If there are multiple constructors and exactly one of them is annotated
   * with <code>javax.inject.Inject</code> then this one is returned.
   * <p>
   * If both of these conditions do not hold true it is checked whether there
   * is a default/no args constructor. If there is one then this one is returned.
   * <p>
   * Otherwise null is returned.
   *
   * @param clazz The clazz to analyse for injectable constructors.
   * @param <A>   The type of that class and of the instances created by the constructor.
   * @return A constructor determined as per the description of this method or null otherwise.
   * @since 1.0.0
   */
  public static <A> Constructor<A> getInjectableConstructor(final Class<A> clazz) {

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

  /**
   * Turns a Constructor into a Supplier.
   * <p>
   * The supplier will not throw any exceptions. It ignores them. In case of
   * an exception the Supplier returned by this method will return null.
   * <p>
   * Note that the supplier does only catch Exceptions, not Throwables.
   *
   * @param constructor The Constructor to be turned into a Supplier.
   * @param <A>         The type of the instances created by the constructor/supplied by the returned Supplier.
   * @return A supplier which invokes the constructor and swallows exceptions.
   * @since 1.0.0
   */
  public static <A> Supplier<A> constructorToSupplier(final Constructor<A> constructor) {
    return () -> {
      try {
        return constructor.newInstance();
      } catch (final Exception exc) {
        return null;
      }
    };
  }

  public static <K, V> Entry<K, V> entry(final K key, final V value) {
    return new Entry<>(key, value);
  }

  public static <T> Cons<T> cons(final T value, final Cons<T> cons) {
    return new Cons<>(value, cons);
  }

}
