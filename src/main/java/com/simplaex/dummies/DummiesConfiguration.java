package com.simplaex.dummies;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.function.Consumer;

@Value
@Wither
@Builder
class DummiesConfiguration {

  private final int recursionMaxDepth;

  private final Consumer<Exception> exceptionHandler;

  public static final DummiesConfiguration defaultConfiguration;

  static {
    defaultConfiguration = DummiesConfiguration.builder()
        .recursionMaxDepth(10)
        .exceptionHandler(exc -> {})
        .build();
  }

}
