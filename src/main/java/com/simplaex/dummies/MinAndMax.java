package com.simplaex.dummies;

import com.simplaex.dummies.annotation.DummyValues;
import lombok.Value;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Value
final class MinAndMax<T> {

  private T min;

  private T max;

  static <A> MinAndMax<A> readMinAndMax(
      final DummyValues dummyValues,
      final Function<DummyValues, A> getMin,
      final Function<DummyValues, A> getMax,
      final Function<String, A> fromString,
      final BiFunction<A, A, Boolean> greaterThan,
      final A nullValue,
      final A defaultMin,
      final A defaultMax
  ) {
    Optional<A> minGiven = Optional
        .of(getMin.apply(dummyValues))
        .filter(value -> greaterThan.apply(value, nullValue));
    Optional<A> maxGiven = Optional
        .of(getMax.apply(dummyValues))
        .filter(value -> greaterThan.apply(value, nullValue));
    if (!minGiven.isPresent() && !dummyValues.min().isEmpty()) {
      minGiven = Optional.of(fromString.apply(dummyValues.min()));
    }
    if (!maxGiven.isPresent() && !dummyValues.max().isEmpty()) {
      maxGiven = Optional.of(fromString.apply(dummyValues.max()));
    }
    if (maxGiven.isPresent() != minGiven.isPresent()) {
      if (maxGiven.isPresent()) {
        minGiven = Optional.of(defaultMin);
      } else {
        maxGiven = Optional.of(defaultMax);
      }
    }
    if (maxGiven.isPresent()) {
      final A min = minGiven.get();
      final A max = maxGiven.get();
      if (greaterThan.apply(max, min)) {
        return new MinAndMax<>(min, max);
      } else {
        return new MinAndMax<>(max, min);
      }
    }
    return null;
  }

}
