package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;

import java.net.InetAddress;

public class InetAddressGenerator implements Generator<InetAddress> {

  private final Inet4AddressGenerator inet4AddressGenerator;
  private final Inet6AddressGenerator inet6AddressGenerator;
  private final Dummies dummies;

  public InetAddressGenerator(final Dummies dummies) {
    this.dummies = dummies;
    this.inet4AddressGenerator = new Inet4AddressGenerator(dummies);
    this.inet6AddressGenerator = new Inet6AddressGenerator(dummies);
  }

  @Override
  public InetAddress get() {
    if (dummies.getRandom().nextBoolean()) {
      return inet4AddressGenerator.get();
    } else {
      return inet6AddressGenerator.get();
    }
  }
}
