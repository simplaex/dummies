package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

@RequiredArgsConstructor
public class Inet6AddressGenerator implements Generator<Inet6Address> {

  private final Dummies dummies;

  @Override
  @SneakyThrows
  public Inet6Address get() {
    val bytes = new byte[16];
    dummies.getRandom().nextBytes(bytes);
    return (Inet6Address) InetAddress.getByAddress(bytes);
  }

}
