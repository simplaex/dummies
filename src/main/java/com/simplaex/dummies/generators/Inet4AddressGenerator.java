package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.net.Inet4Address;
import java.net.InetAddress;

@RequiredArgsConstructor
public class Inet4AddressGenerator implements Generator<Inet4Address> {

  private final Dummies dummies;

  @Override
  @SneakyThrows
  public Inet4Address get() {
    val bytes = new byte[4];
    dummies.getRandom().nextBytes(bytes);
    return (Inet4Address) InetAddress.getByAddress(bytes);
  }

}
