package com.simplaex.dummies.util;

/**
 * A RuntimeException that does not carry a stack trace and is thus more
 * lightweight.
 *
 * @since 1.0.0
 */
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
