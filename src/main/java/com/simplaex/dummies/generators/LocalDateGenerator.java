package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LocalDateGenerator implements Generator<LocalDate> {

  private final Dummies dummies;

  private final long epochDayToday = LocalDate.now().toEpochDay();

  @Override
  public LocalDate get() {
    return LocalDate.ofEpochDay(Math.abs(dummies.getRandom().nextLong()) % epochDayToday);
  }
}
