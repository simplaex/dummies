package com.simplaex.dummies;

import com.simplaex.dummies.util.Lazy;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.simplaex.dummies.util.UtilityBelt.constructorToSupplier;
import static com.simplaex.dummies.util.UtilityBelt.lazy;

@Value
@EqualsAndHashCode(of = {"type", "genericType"})
class TypeInfo<T> {

  private Class<T> type;

  private Type genericType;

  @Getter(AccessLevel.NONE)
  private Lazy<List<TypeInfo<?>>> lazyGenericParameters = lazy(
      () -> {
        final Type genericType = getGenericType();
        final List<TypeInfo<?>> types = new ArrayList<>();
        if (genericType instanceof ParameterizedType) {
          final ParameterizedType parameterizedType = (ParameterizedType) genericType;
          final Type[] typeArguments = parameterizedType.getActualTypeArguments();

          for (final Type typeParameter : typeArguments) {
            if (typeParameter instanceof Class) {
              final Class<?> typeParameterClass = (Class<?>) typeParameter;
              types.add(TypeInfo.get(typeParameterClass, typeParameter));
            } else if (typeParameter instanceof ParameterizedType) {
              final ParameterizedType typeParameterType = (ParameterizedType) typeParameter;
              types.add(TypeInfo.get((Class<?>) typeParameterType.getRawType(), typeParameterType));
            }
          }
        }
        return types;
      }
  );

  private static Map<Class<?>, Supplier<?>> knownSuppliers = new HashMap<>();

  static {
    knownSuppliers.put(Set.class, HashSet::new);
    knownSuppliers.put(List.class, ArrayList::new);
    knownSuppliers.put(Map.class, HashMap::new);
    knownSuppliers.put(Deque.class, ArrayDeque::new);
    knownSuppliers.put(Queue.class, ArrayDeque::new);
  }

  @SuppressWarnings("unchecked")
  @Getter(AccessLevel.NONE)
  private Lazy<Supplier<T>> zeroArgsConstructor = lazy(
      (Supplier<Supplier<T>>) () -> {
        try {
          return constructorToSupplier(getType().getConstructor());
        } catch (final NoSuchMethodException e) {
          if (knownSuppliers.containsKey(getType())) {
            return (Supplier<T>) knownSuppliers.get(getType());
          }
          return null;
        }
      }
  );

  private static Map<Class<?>, Function<String, ?>> knownFactoryMethods = new HashMap<>();

  static {
    try {
      final Method method = Paths.class.getMethod("get", String.class, String[].class);
      final String[] empty = new String[0];
      knownFactoryMethods.put(Path.class, stringValue -> {
        try {
          return method.invoke(null, stringValue, empty);
        } catch (final Exception exc) {
          return null;
        }
      });
    } catch (final NoSuchMethodException ignore) {
    }
  }

  @Getter(AccessLevel.NONE)
  private Lazy<Function<String, T>> fromStringFactory = lazy(
      (Supplier<Function<String, T>>) () -> {
        if (knownFactoryMethods.containsKey(getType())) {
          //noinspection unchecked
          return (Function<String, T>) knownFactoryMethods.get(getType());
        }
        try {
          final Constructor<T> constructor = getType().getConstructor(String.class);
          return stringValue -> {
            try {
              return constructor.newInstance(stringValue);
            } catch (final Exception exc) {
              return null;
            }
          };
        } catch (final NoSuchMethodException ignore) {
        }
        final List<Class<?>> allowed = Arrays.asList(
            String.class,
            CharSequence.class,
            byte[].class
        );
        final List<Method> allMethods =
            Arrays
                .stream(getType().getMethods())
                .filter(method ->
                    (method.getModifiers() & Modifier.STATIC) != 0
                        && (method.getModifiers() & Modifier.PUBLIC) != 0
                        && method.getParameterCount() == 1
                        && getType().isAssignableFrom(method.getReturnType())
                        && allowed.contains(method.getParameterTypes()[0]))
                .collect(Collectors.toList());
        final List<Method> stringMethods = allMethods.stream()
            .filter(method -> method.getParameterTypes()[0].equals(String.class))
            .collect(Collectors.toList());
        final List<Method> charSequenceMethods = allMethods.stream()
            .filter(method -> method.getParameterTypes()[0].equals(CharSequence.class))
            .collect(Collectors.toList());
        final List<Method> byteArrayMethods = allMethods.stream()
            .filter(method -> method.getParameterTypes()[0].equals(byte[].class))
            .collect(Collectors.toList());
        if (stringMethods.size() == 1 || charSequenceMethods.size() == 1) {
          final Method method;
          if (stringMethods.size() == 1) {
            method = stringMethods.get(0);
          } else {
            method = charSequenceMethods.get(0);
          }
          return stringValue -> {
            try {
              //noinspection unchecked
              return (T) method.invoke(null, stringValue);
            } catch (final Exception e) {
              return null;
            }
          };
        } else if (byteArrayMethods.size() == 1) {
          final Method method = byteArrayMethods.get(0);
          return stringValue -> {
            try {
              //noinspection unchecked
              return (T) method.invoke(null, (Object) stringValue.getBytes(StandardCharsets.UTF_8));
            } catch (final Exception e) {
              return null;
            }
          };
        }

        return null;
      }
  );

  boolean isString() {
    return type.equals(String.class);
  }

  boolean isChar() {
    return type.equals(char.class) || type.equals(Character.class);
  }

  boolean isDouble() {
    return type.equals(double.class) || type.equals(Double.class);
  }

  boolean isFloat() {
    return type.equals(float.class) || type.equals(Float.class);
  }

  boolean isInteger() {
    return type.equals(int.class) || type.equals(Integer.class);
  }

  boolean isLong() {
    return type.equals(long.class) || type.equals(Long.class);
  }

  boolean isShort() {
    return type.equals(short.class) || type.equals(Short.class);
  }

  boolean isByte() {
    return type.equals(byte.class) || type.equals(Byte.class);
  }

  boolean isBoolean() {
    return type.equals(boolean.class) || type.equals(Boolean.class);
  }

  boolean isArray() {
    return type.isArray();
  }

  boolean isEnum() {
    return type.isEnum();
  }

  boolean isCollection() {
    return Collection.class.isAssignableFrom(type);
  }

  boolean isMap() {
    return Map.class.isAssignableFrom(type);
  }

  Class<?> getBoxedType() {
    if (type.isPrimitive()) {
      if (isChar()) {
        return Character.class;
      } else if (isDouble()) {
        return Double.class;
      } else if (isFloat()) {
        return Float.class;
      } else if (isInteger()) {
        return Integer.class;
      } else if (isLong()) {
        return Long.class;
      } else if (isShort()) {
        return Short.class;
      } else if (isByte()) {
        return Byte.class;
      } else if (isBoolean()) {
        return Boolean.class;
      }
    }
    return type;
  }

  T newInstance() {
    return Optional.ofNullable(zeroArgsConstructor.get()).map(Supplier::get).orElse(null);
  }

  boolean hasFromStringFactory() {
    return fromStringFactory.get() != null;
  }

  T fromString(final String stringValue) {
    return Optional.ofNullable(fromStringFactory.get()).map(factory -> {
      try {
        return factory.apply(stringValue);
      } catch (final Exception exc) {
        return null;
      }
    }).orElse(null);
  }

  List<TypeInfo<?>> getGenericParameters() {
    return lazyGenericParameters.get();
  }

  private static final Map<Class<?>, TypeInfo<?>> map =
      Collections.synchronizedMap(new HashMap<>());

  static <A> TypeInfo<A> get(final Class<A> clazz) {
    return new TypeInfo<>(clazz, clazz);
  }

  static <A> TypeInfo<A> get(final Class<A> clazz, final Type genericType) {
    if (clazz == genericType) {
      //noinspection unchecked
      return (TypeInfo<A>) map.computeIfAbsent(clazz, TypeInfo::get);
    }
    return new TypeInfo<>(clazz, genericType);
  }

}
