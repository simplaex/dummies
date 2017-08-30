package com.simplaex.dummies;

import com.simplaex.dummies.annotation.DummyValues;
import lombok.Value;

@Value
final class DummyAnnotations {

  DummyValues defaultValues;

  DummyValues keyValues;

  DummyValues valueValues;

  DummyAnnotations forValues() {
    return new DummyAnnotations(valueValues, null, null);
  }

  DummyAnnotations forKeys() {
    return new DummyAnnotations(keyValues, null, null);
  }

  static DummyAnnotations empty() {
    return new DummyAnnotations(null, null, null);
  }

  static DummyAnnotations getDefault() {
    return new DummyAnnotations(DefaultDummyValues.get(), null, null);
  }

}
