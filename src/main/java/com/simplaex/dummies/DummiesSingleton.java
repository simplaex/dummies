package com.simplaex.dummies;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
final class DummiesSingleton {

  static final Dummies INSTANCE = Dummies.builder().build();

}