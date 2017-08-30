package com.simplaex.dummies;

import com.simplaex.dummies.annotation.DummySpec;
import com.simplaex.dummies.annotation.DummyTarget;
import com.simplaex.dummies.annotation.DummyValues;
import com.simplaex.dummies.generators.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.simplaex.dummies.util.UtilityBelt.getInjectableConstructor;

final class DummiesImpl implements Dummies {

  private final Supplier<Random> randomSupplier;

  private final Consumer<Exception> exceptionHandler;

  private final Map<Class<?>, ClassInfo> classInfo =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<Class<?>, Supplier<?>> classFactories =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<Class<?>, Generator<?>> generators = new HashMap<>();

  DummiesImpl(
      final Supplier<Random> randomSupplier,
      final Consumer<Exception> exceptionHandler
  ) {
    this.randomSupplier = randomSupplier;
    this.exceptionHandler = exceptionHandler;

    addGenerator(UUID.class, UUIDGenerator.class);
    addGenerator(Character.class, () -> (char) getRandom().nextInt(Character.MAX_VALUE));
    addGenerator(Byte.class, () -> (byte) getRandom().nextInt());
    addGenerator(Short.class, () -> (short) getRandom().nextInt());
    addGenerator(Integer.class, () -> getRandom().nextInt());
    addGenerator(Long.class, () -> getRandom().nextLong());
    addGenerator(Float.class, () -> getRandom().nextFloat());
    addGenerator(Double.class, () -> getRandom().nextDouble());
    addGenerator(Boolean.class, () -> getRandom().nextBoolean());
    addGenerator(Random.class, randomSupplier::get);
    addGenerator(BigInteger.class, () -> BigInteger.valueOf(getRandom().nextInt(Integer.MAX_VALUE)));
    addGenerator(BigDecimal.class, () -> BigDecimal.valueOf(getRandom().nextDouble() * Integer.MAX_VALUE));
    addGenerator(Date.class, () -> new Date(Math.abs(getRandom().nextLong())));
    addGenerator(Instant.class, () -> Instant.ofEpochMilli(Math.abs(getRandom().nextLong())));
    addGenerator(LocalTime.class, new LocalTimeGenerator(this));
    addGenerator(LocalDate.class, new LocalDateGenerator(this));
    addGenerator(LocalDateTime.class, new LocalDateTimeGenerator(this));
    addGenerator(InetAddress.class, new InetAddressGenerator(this));
    addGenerator(Inet4Address.class, new Inet4AddressGenerator(this));
    addGenerator(Inet6Address.class, new Inet6AddressGenerator(this));
  }

  private void handleException(final Exception exc) {
    try {
      exceptionHandler.accept(exc);
    } catch (final Exception ignore) {
    }
  }

  void addGenerator(final Class<?> clazz, final Generator<?> generator) {
    generators.put(clazz, generator);
  }

  void addGenerator(final Class<?> clazz, final Class<?> generatorClass) {
    addGenerator(clazz, newGenerator(generatorClass));
  }

  @SuppressWarnings("unchecked")
  private <T> Generator<T> newGenerator(final Class<T> generator) {
    List<Constructor<Generator<T>>> constructors = Arrays
        .stream(generator.getConstructors())
        .map(constructor -> (Constructor<Generator<T>>) constructor)
        .filter(constructor -> {
          if (constructor.getParameterCount() != 1) {
            return false;
          }
          final Class<?> baseType = constructor.getParameterTypes()[0];
          return Dummies.class.isAssignableFrom(baseType);
        })
        .collect(Collectors.toList());
    if (constructors.size() != 1) {
      throw new InstantiationException("must have exactly one constructor that takes a DummiesImpl object as its sole argument.");
    }
    try {
      return constructors.get(0).newInstance(this);
    } catch (final Exception exc) {
      throw new InstantiationException(exc);
    }
  }


  private List<?> extractValueList(final DummyValues dummyValues, final TypeInfo typeInfo) {

    if (typeInfo.isString()) {
      return Arrays.asList(dummyValues.value());

    } else if (typeInfo.isBoolean()) {
      return Arrays.asList(true, false);

    } else if (typeInfo.isDouble()) {
      final Double[] array = new Double[dummyValues.doubleValues().length];
      for (int i = 0; i < dummyValues.doubleValues().length; i += 1) {
        array[i] = dummyValues.doubleValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isFloat()) {
      final Float[] array = new Float[dummyValues.floatValues().length];
      for (int i = 0; i < dummyValues.floatValues().length; i += 1) {
        array[i] = dummyValues.floatValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isByte()) {
      final Byte[] array = new Byte[dummyValues.byteValues().length];
      for (int i = 0; i < dummyValues.byteValues().length; i += 1) {
        array[i] = dummyValues.byteValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isShort()) {
      final Short[] array = new Short[dummyValues.shortValues().length];
      for (int i = 0; i < dummyValues.shortValues().length; i += 1) {
        array[i] = dummyValues.shortValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isInteger()) {
      final Integer[] array = new Integer[dummyValues.intValues().length];
      for (int i = 0; i < dummyValues.intValues().length; i += 1) {
        array[i] = dummyValues.intValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isLong()) {
      final Long[] array = new Long[dummyValues.longValues().length];
      for (int i = 0; i < dummyValues.longValues().length; i += 1) {
        array[i] = dummyValues.longValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.isChar()) {
      final Character[] array = new Character[dummyValues.charValues().length];
      for (int i = 0; i < dummyValues.charValues().length; i += 1) {
        array[i] = dummyValues.charValues()[i];
      }
      return Arrays.asList(array);

    } else if (typeInfo.hasFromStringFactory()) {
      return Arrays
          .stream(dummyValues.value())
          .map((Function<String, Object>) typeInfo::fromString)
          .collect(Collectors.toList());

    } else {
      return Collections.emptyList();
    }
  }

  private Generator<?> createGeneratorForRange(
      final DummyValues dummyValues,
      final TypeInfo typeInfo
  ) {

    if (typeInfo.isDouble()) {
      final MinAndMax<Double> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::doubleMin, DummyValues::doubleMax,
          Double::valueOf, (a, b) -> a > b,
          Double.MIN_VALUE, Double.MIN_VALUE + 1.0, Double.MAX_VALUE
      );
      if (minAndMax != null) {
        final double min = minAndMax.getMin();
        final double max = minAndMax.getMax();
        final double scale = Math.abs(max - min);
        final Generator<Double> generator =
            () -> min + randomSupplier.get().nextDouble() * scale;

        if (dummyValues.precision() >= 0) {
          final double exp = Math.pow(10, dummyValues.precision());
          return () -> Math.round(generator.get() * exp) / exp;
        } else {
          return generator;
        }
      }

    } else if (typeInfo.isFloat()) {
      final MinAndMax<Float> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::floatMin, DummyValues::floatMax,
          Float::valueOf, (a, b) -> a > b,
          Float.MIN_VALUE, Float.MIN_VALUE + 1.0f, Float.MAX_VALUE
      );
      if (minAndMax != null) {
        final float min = minAndMax.getMin();
        final float max = minAndMax.getMax();
        final float scale = Math.abs(max - min);
        final Generator<Float> generator =
            () -> min + randomSupplier.get().nextFloat() * scale;

        if (dummyValues.precision() >= 0) {
          final float exp = (float) Math.pow(10, dummyValues.precision());
          return () -> Math.round(generator.get() * exp) / exp;
        } else {
          return generator;
        }
      }

    } else if (typeInfo.isByte()) {
      final MinAndMax<Byte> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::byteMin, DummyValues::byteMax,
          Byte::valueOf, (a, b) -> a > b,
          Byte.MIN_VALUE, (byte) (Byte.MIN_VALUE + 1), Byte.MAX_VALUE
      );
      if (minAndMax != null) {
        final int min = minAndMax.getMin();
        final int max = minAndMax.getMax();
        final int intMax = Math.abs(max - min);

        return () -> (byte) (min + randomSupplier.get().nextInt(intMax));
      }

    } else if (typeInfo.isShort()) {
      final MinAndMax<Short> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::shortMin, DummyValues::shortMax,
          Short::valueOf, (a, b) -> a > b,
          Short.MIN_VALUE, (short) (Short.MIN_VALUE + 1), Short.MAX_VALUE
      );
      if (minAndMax != null) {
        final int min = minAndMax.getMin();
        final int max = minAndMax.getMax();
        final int intMax = Math.abs(max - min);

        return () -> (short) (min + randomSupplier.get().nextInt(intMax));
      }

    } else if (typeInfo.isInteger()) {
      final MinAndMax<Integer> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::intMin, DummyValues::intMax,
          Integer::valueOf, (a, b) -> a > b,
          Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MAX_VALUE
      );
      if (minAndMax != null) {
        final long min = minAndMax.getMin();
        final long max = minAndMax.getMax();
        final long longMax = max - min;

        return () -> {
          final long nextLong = Math.abs(getRandom().nextLong());
          final long nextInt = nextLong % longMax;
          return (int) nextInt;
        };
      }

    } else if (typeInfo.isLong()) {
      final MinAndMax<Long> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::longMin, DummyValues::longMax,
          Long::valueOf, (a, b) -> a > b,
          Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MAX_VALUE
      );
      if (minAndMax != null) {
        final long min = minAndMax.getMin();
        final long max = minAndMax.getMax();
        final long middle = (max - min) / 2L + min;
        final long distance = max - middle;

        return () -> {
          final Random random = getRandom();
          final long sign = random.nextInt() >= 0 ? 1 : -1;
          return middle + (sign * (random.nextLong() % distance));
        };
      }

    } else if (typeInfo.isChar()) {
      final MinAndMax<Character> minAndMax = MinAndMax.readMinAndMax(
          dummyValues, DummyValues::charMin, DummyValues::charMax,
          string -> string.charAt(0), (a, b) -> a > b,
          Character.MIN_VALUE, (char) 1, Character.MAX_VALUE
      );
      if (minAndMax != null) {
        final long min = minAndMax.getMin();
        final long max = minAndMax.getMax();
        final long longMax = max - min;

        return () -> (char) (min + (getRandom().nextLong() % longMax));
      }
    }

    return null;
  }

  private IntSupplier createLengthSupplierFor(
      final DummyValues dummyValues
  ) {
    final MinAndMax<Integer> minAndMax = MinAndMax.readMinAndMax(
        dummyValues, DummyValues::minLength, DummyValues::maxLength,
        Integer::valueOf, (a, b) -> a > b,
        -1, 0, 10);
    if (minAndMax != null) {
      final int min = minAndMax.getMin();
      final int max = minAndMax.getMax();
      final int intMax = max - min;
      return () -> min + randomSupplier.get().nextInt(intMax);
    } else if (dummyValues.length() >= 0) {
      final int length = dummyValues.length();
      return () -> length;
    } else {
      return () -> 0;
    }
  }

  private <A> Generator<A> createGeneratorForArray(
      final DummyAnnotations dummyAnnotations,
      final TypeInfo<A> typeInfo
  ) {
    final DummyValues dummyValues = dummyAnnotations.getDefaultValues();
    final Class<?> componentType = typeInfo.getType().getComponentType();
    final TypeInfo<?> componentTypeInfo = TypeInfo.get(componentType, componentType);
    final DummyAnnotations valueAnnotations;
    if (dummyAnnotations.getValueValues() != null) {
      valueAnnotations = dummyAnnotations.forValues();
    } else {
      valueAnnotations = dummyAnnotations;
    }
    final Generator<?> elementGenerator = createGeneratorFor(valueAnnotations, componentTypeInfo);
    final IntSupplier lengthSupplier = createLengthSupplierFor(dummyValues);
    return () -> {
      final int length = lengthSupplier.getAsInt();
      final Object array = Array.newInstance(componentType, length);
      for (int i = 0; i < length; i += 1) {
        Array.set(array, i, elementGenerator.get());
      }
      //noinspection unchecked
      return (A) array;
    };
  }

  private <A> Generator<A> createGeneratorForCollection(
      final DummyAnnotations dummyAnnotations,
      final TypeInfo<A> typeInfo
  ) {
    final DummyValues dummyValues = dummyAnnotations.getDefaultValues();
    final TypeInfo<?> genericTypeInfo = typeInfo.getGenericParameters().get(0);
    final DummyAnnotations valueAnnotations;
    if (dummyAnnotations.getValueValues() != null) {
      valueAnnotations = dummyAnnotations.forValues();
    } else {
      valueAnnotations = dummyAnnotations;
    }
    final Generator<?> elementGenerator = createGeneratorFor(valueAnnotations, genericTypeInfo);
    final IntSupplier lengthSupplier = createLengthSupplierFor(dummyValues);
    return () -> {
      @SuppressWarnings("unchecked") final Collection<Object> collection = (Collection<Object>) typeInfo.newInstance();
      final int desiredLength = lengthSupplier.getAsInt();
      while (collection.size() < desiredLength) {
        final Object value = elementGenerator.get();
        collection.add(value);
      }
      //noinspection unchecked
      return (A) collection;
    };
  }

  private Generator<?> createGeneratorForMap(
      final DummyAnnotations dummyAnnotations,
      final TypeInfo<?> typeInfo
  ) {
    final DummyValues dummyValues = dummyAnnotations.getDefaultValues();
    final TypeInfo keyTypeInfo = typeInfo.getGenericParameters().get(0);
    final TypeInfo valueTypeInfo = typeInfo.getGenericParameters().get(1);
    final Generator<?> keyGenerator = createGeneratorFor(dummyAnnotations.forKeys(), keyTypeInfo);
    final Generator<?> valueGenerator = createGeneratorFor(dummyAnnotations.forValues(), valueTypeInfo);
    final IntSupplier lengthSupplier = createLengthSupplierFor(dummyValues);
    return () -> {
      @SuppressWarnings("unchecked") final Map<Object, Object> map = (Map<Object, Object>) typeInfo.newInstance();
      final int desiredLength = lengthSupplier.getAsInt();
      while (map.size() < desiredLength) {
        final Object key = keyGenerator.get();
        final Object value = valueGenerator.get();
        map.put(key, value);
      }
      return map;
    };
  }

  private <A> Generator<A> createGeneratorForEnum(final TypeInfo<A> typeInfo) {
    final A[] enumConstants = typeInfo.getType().getEnumConstants();
    return () -> enumConstants[getRandom().nextInt(enumConstants.length)];
  }

  private Generator<?> createGeneratorFor(
      final DummyAnnotations dummyAnnotations,
      final TypeInfo<?> typeInfo
  ) {
    final DummyValues dummyValues = dummyAnnotations.getDefaultValues();
    if (dummyValues != null) {
      if (!dummyValues.generator().equals(Generator.NullGenerator.class)) {
        final Generator<?> generator = newGenerator(dummyValues.generator());
        if (typeInfo.isString()) {
          return () -> generator.get().toString();
        }
        return generator;
      }
      if (typeInfo.isArray()) {
        return createGeneratorForArray(dummyAnnotations, typeInfo);
      } else if (typeInfo.isCollection() && typeInfo.getGenericParameters().size() == 1) {
        return createGeneratorForCollection(dummyAnnotations, typeInfo);
      } else if (typeInfo.isMap()
          && typeInfo.getGenericParameters().size() == 2
          && dummyAnnotations.getValueValues() != null
          && dummyAnnotations.getKeyValues() != null
          ) {
        return createGeneratorForMap(dummyAnnotations, typeInfo);
      } else if (typeInfo.isEnum()) {
        return createGeneratorForEnum(typeInfo);
      }
      final List<?> valueList = extractValueList(dummyValues, typeInfo);
      if (!valueList.isEmpty()) {
        return () -> valueList.get(randomSupplier.get().nextInt(valueList.size()));
      }
      final Generator<?> rangeGenerator = createGeneratorForRange(dummyValues, typeInfo);
      if (rangeGenerator != null) {
        return rangeGenerator;
      }
    }
    if (generators.containsKey(typeInfo.getBoxedType())) {
      return generators.get(typeInfo.getBoxedType());
    }
    return () -> create(typeInfo.getType());
  }

  private Generator<?> createGeneratorForAnnotatedType(
      final AnnotatedType annotatedType
  ) {
    Optional<DummySpec> annotation = annotatedType.getAnnotation(DummySpec.class);
    if (!annotation.isPresent()) {
      final Optional<DummyValues> dummyValuesAnnotation = annotatedType.getAnnotation(DummyValues.class);
      if (dummyValuesAnnotation.isPresent()) {
        annotation = Optional.of(new DummySpec() {
          @Override
          public Class<? extends Annotation> annotationType() {
            return DummySpec.class;
          }

          @Override
          public DummyValues[] value() {
            return new DummyValues[]{dummyValuesAnnotation.get()};
          }
        });
      }
    }
    final DummyAnnotations dummyAnnotations = annotation.map(dummySpec -> {
      final Map<DummyTarget, DummyValues> dummyValuesMap = new HashMap<>();
      for (final DummyValues dummyValues : dummySpec.value()) {
        dummyValuesMap.put(dummyValues.target(), dummyValues);
      }
      return new DummyAnnotations(
          dummyValuesMap.get(DummyTarget.DEFAULT),
          dummyValuesMap.get(DummyTarget.KEYS),
          dummyValuesMap.get(DummyTarget.VALUES)
      );
    }).orElse(DummyAnnotations.empty());
    return createGeneratorFor(dummyAnnotations, annotatedType.getTypeInfo());
  }

  private PropertyInfo<?> createPropertyInfoFor(final AnnotatedType annotatedType, final Method writeMethod) {
    Generator<?> generator;
    try {
      generator = createGeneratorForAnnotatedType(annotatedType);
    } catch (final Exception exc) {
      handleException(exc);
      generator = () -> null;
    }
    //noinspection unchecked
    return PropertyInfo.create(annotatedType, generator, writeMethod);
  }

  private <T> LinkedHashMap<String, FieldInfo<?>> createFieldInfoMapFor(final Class<T> clazz) {

    final Constructor<T> constructor = getInjectableConstructor(clazz);
    if (constructor == null) {
      return new LinkedHashMap<>();
    }
    final Parameter[] parameters = constructor.getParameters();
    final Field[] fields = clazz.getDeclaredFields();
    final LinkedHashMap<String, FieldInfo<?>> fieldInfoMap = new LinkedHashMap<>();
    for (int pi = 0, fi = 0; pi < parameters.length && fi < fields.length; fi += 1) {

      final Parameter p = parameters[pi];
      final Field f = fields[fi];

      final TypeInfo<?> pt = TypeInfo.get(p.getType(), p.getParameterizedType());
      final TypeInfo<?> ft = TypeInfo.get(f.getType(), f.getGenericType());

      if (!pt.equals(ft)) {
        continue;
      }
      if (f.isSynthetic()) {
        continue;
      }

      final Map<Class<? extends Annotation>, Annotation> annotationMap = new HashMap<>();
      for (final Annotation annotation : f.getAnnotations()) {
        annotationMap.put(annotation.annotationType(), annotation);
      }

      final AnnotatedType annotatedType = new AnnotatedType(f.getName(), pt, annotationMap);

      Generator<?> generator;
      try {
        generator = createGeneratorForAnnotatedType(annotatedType);
      } catch (final Exception exc) {
        handleException(exc);
        generator = () -> null;
      }

      final FieldInfo<?> fieldInfo = FieldInfo.create(annotatedType, generator);

      fieldInfoMap.put(f.getName(), fieldInfo);

      pi += 1;
    }

    return fieldInfoMap;
  }

  private ClassInfo createClassInfoFor(final Class<?> clazz) {
    final LinkedHashMap<String, PropertyInfo<?>> properties = new LinkedHashMap<>();
    final LinkedHashMap<String, FieldInfo<?>> fieldInfoMap = createFieldInfoMapFor(clazz);
    final BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(clazz);
    } catch (final IntrospectionException exc) {
      handleException(exc);
      return new ClassInfo(clazz, new LinkedHashMap<>(), fieldInfoMap);
    }

    final ArrayList<Annotation> annotations = new ArrayList<>();
    for (final PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
      try {
        final Method readMethod = pd.getReadMethod();
        final Method writeMethod = pd.getWriteMethod();
        final String name = pd.getName();
        try {
          final Field field = clazz.getDeclaredField(name);
          annotations.addAll(Arrays.asList(field.getAnnotations()));
        } catch (final NoSuchFieldException ignore) {
        }
        if (readMethod != null &&
            writeMethod != null &&
            (readMethod.getModifiers() & Modifier.PUBLIC) != 0 &&
            (writeMethod.getModifiers() & Modifier.PUBLIC) != 0 &&
            writeMethod.getParameterCount() == 1 &&
            writeMethod.getParameterTypes()[0].isAssignableFrom(readMethod.getReturnType())) {
          annotations.addAll(Arrays.asList(readMethod.getAnnotations()));
          annotations.addAll(Arrays.asList(writeMethod.getAnnotations()));
          final Map<Class<? extends Annotation>, Annotation> annotationMap =
              new HashMap<>();
          for (final Annotation annotation : annotations) {
            annotationMap.put(annotation.annotationType(), annotation);
          }
          final TypeInfo<?> typeInfo = TypeInfo.get(
              readMethod.getReturnType(),
              readMethod.getGenericReturnType()
          );
          final AnnotatedType propertyDescription = new AnnotatedType(
              name, typeInfo, annotationMap
          );
          final PropertyInfo propertyInfo = createPropertyInfoFor(
              propertyDescription, writeMethod
          );
          properties.put(name, propertyInfo);
        }
      } finally {
        annotations.clear();
      }
    }
    return new ClassInfo(clazz, properties, fieldInfoMap);
  }

  private ClassInfo getClassInfoFor(final Class<?> clazz) {
    return classInfo.computeIfAbsent(clazz, this::createClassInfoFor);
  }

  private <A> Supplier<A> createFactoryFor(final Class<A> clazz) {
    if (generators.containsKey(clazz)) {
      //noinspection unchecked
      return (Generator<A>) generators.get(clazz);
    }
    final TypeInfo<A> targetTypeInfo = TypeInfo.get(clazz);
    if (targetTypeInfo.isArray()) {
      return createGeneratorForArray(DummyAnnotations.getDefault(), targetTypeInfo);
    } else if (targetTypeInfo.isEnum()) {
      return createGeneratorForEnum(targetTypeInfo);
    }
    final Constructor<A> constructor = getInjectableConstructor(clazz);
    if (constructor == null) {
      throw new InstantiationException("must declare exactly one injectable constructor or a generator defined for it (" + clazz + ")");
    }
    final ClassInfo classInfo = getClassInfoFor(clazz);
    final Map<String, FieldInfo<?>> fieldInfoMap = classInfo.getFields();
    final Parameter[] parameters = constructor.getParameters();

    final Generator<?>[] generators = new Generator[parameters.length];

    if (fieldInfoMap.size() == parameters.length) {
      int i = 0;
      for (final FieldInfo<?> fieldInfo : fieldInfoMap.values()) {
        generators[i++] = fieldInfo.getGenerator();
      }
    } else {
      int i = 0;
      for (final Parameter parameter : parameters) {
        final TypeInfo<?> typeInfo = TypeInfo.get(parameter.getType(), parameter.getParameterizedType());
        generators[i++] = createGeneratorFor(DummyAnnotations.empty(), typeInfo);
      }
    }

    return () -> {
      final Object[] args = new Object[generators.length];
      for (int i = 0; i < args.length; i += 1) {
        args[i] = generators[i].get();
      }
      try {
        return constructor.newInstance(args);
      } catch (final Exception exc) {
        handleException(exc);
        return null;
      }
    };

  }

  private <T> Supplier<T> getFactoryFor(final Class<T> clazz) {
    //noinspection unchecked
    return (Supplier<T>) classFactories.computeIfAbsent(clazz, this::createFactoryFor);
  }

  @Override
  public <T> T fill(final T obj) {
    final ClassInfo classInfo = getClassInfoFor(obj.getClass());
    for (final PropertyInfo p : classInfo) {
      try {
        final Object value = p.getGenerator().get();
        if (value != null) {
          p.getWriteMethod().invoke(obj, value);
        }
      } catch (final Exception exc) {
        handleException(exc);
      }
    }
    return obj;
  }

  @Override
  public <T> T create(final Class<T> clazz) {
    return fill(getFactoryFor(clazz).get());
  }

  @Override
  public <T> T fromString(final Class<T> clazz, final String string) {
    final TypeInfo<T> typeInfo = TypeInfo.get(clazz, clazz);
    return typeInfo.fromString(string);
  }

  @Override
  public Random getRandom() {
    return randomSupplier.get();
  }

}
