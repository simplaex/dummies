package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.Random;

@RequiredArgsConstructor
public class LocalTimeGenerator implements Generator<LocalTime> {

  private final Dummies dummies;

  @Override
  public LocalTime get() {
    final Random random = dummies.getRandom();
    return LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));
  }
}
