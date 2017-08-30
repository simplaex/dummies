package com.simplaex.dummies.util;

public class FastRuntimeException extends RuntimeException {

  public FastRuntimeException(final String message) {
    super(message);
  }

  public FastRuntimeException(final Exception cause) {
    super(cause.getMessage(), cause);
  }

  @Override
  public final Throwable fillInStackTrace() {
    return this;
  }
}
