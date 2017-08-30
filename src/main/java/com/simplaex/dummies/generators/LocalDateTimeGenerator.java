package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;

import java.time.LocalDateTime;

public class LocalDateTimeGenerator implements Generator<LocalDateTime> {

  private final LocalTimeGenerator localTimeGenerator;
  private final LocalDateGenerator localDateGenerator;

  public LocalDateTimeGenerator(final Dummies dummies) {
    this.localDateGenerator = new LocalDateGenerator(dummies);
    this.localTimeGenerator = new LocalTimeGenerator(dummies);
  }

  @Override
  public LocalDateTime get() {
    return LocalDateTime.of(localDateGenerator.get(), localTimeGenerator.get());
  }
}
