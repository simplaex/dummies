package com.simplaex.dummies;

import com.simplaex.dummies.util.FastRuntimeException;

public class InstantiationException extends FastRuntimeException {

  InstantiationException(final String message) {
    super(message);
  }

  InstantiationException(final Exception cause) {
    super(cause);
  }
}
