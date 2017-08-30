package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class IpAddressGenerator implements Generator<String> {

  private final Dummies dummies;

  @Override
  public String get() {
    val random = dummies.getRandom();
    val builder = new StringBuilder();
    builder.append(1 + random.nextInt(255));
    builder.append('.');
    builder.append(1 + random.nextInt(255));
    builder.append('.');
    builder.append(1 + random.nextInt(255));
    builder.append('.');
    builder.append(1 + random.nextInt(255));
    return builder.toString();
  }

}
