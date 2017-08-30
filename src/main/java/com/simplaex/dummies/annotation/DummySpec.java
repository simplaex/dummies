package com.simplaex.dummies.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DummySpec {

  DummyValues[] value();

}
