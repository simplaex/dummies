package com.simplaex.dummies;

public class InstantiationException extends FastException {

  InstantiationException(final String message) {
    super(message);
  }

  InstantiationException(final Exception cause) {
    super(cause);
  }
}
