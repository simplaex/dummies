package com.simplaex.dummies;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
final class DummiesSingleton {

  static Consumer<Exception> noOpExceptionHandler = exc -> {};

  static final Dummies INSTANCE = Dummies.builder().build();

}