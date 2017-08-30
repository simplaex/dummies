package com.simplaex.dummies.annotation;

import com.simplaex.dummies.Generator;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DummySpec.class)
public @interface DummyValues {

  DummyTarget target() default DummyTarget.DEFAULT;

  Class<? extends Generator<?>> generator() default Generator.NullGenerator.class;

  int length() default -1;

  int minLength() default -1;

  int maxLength() default -1;


  String[] value() default {};

  char[] charValues() default {};

  byte[] byteValues() default {};

  short[] shortValues() default {};

  int[] intValues() default {};

  long[] longValues() default {};

  float[] floatValues() default {};

  double[] doubleValues() default {};


  String max() default "";

  char charMax() default Character.MIN_VALUE;

  byte byteMax() default Byte.MIN_VALUE;

  short shortMax() default Short.MIN_VALUE;

  int intMax() default Integer.MIN_VALUE;

  long longMax() default Long.MIN_VALUE;

  float floatMax() default Float.MIN_VALUE;

  double doubleMax() default Double.MIN_VALUE;


  String min() default "";

  char charMin() default Character.MIN_VALUE;

  byte byteMin() default Byte.MIN_VALUE;

  short shortMin() default Short.MIN_VALUE;

  int intMin() default Integer.MIN_VALUE;

  long longMin() default Long.MIN_VALUE;

  float floatMin() default Float.MIN_VALUE;

  double doubleMin() default Double.MIN_VALUE;


  int precision() default -1;

}
