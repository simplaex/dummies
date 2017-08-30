package com.simplaex.dummies;

public class FastException extends RuntimeException {

  FastException(final String message) {
    super(message);
  }

  FastException(final Exception cause) {
    super(cause.getMessage(), cause);
  }

  @Override
  public final Throwable fillInStackTrace() {
    return this;
  }
}
