package com.simplaex.dummies.generators;

import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.Generator;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class UUIDGenerator implements Generator<UUID> {

  private final Dummies dummies;

  @Override
  public UUID get() {
    final Random random = dummies.getRandom();
    final byte[] randomBytes = new byte[16];
    random.nextBytes(randomBytes);
    randomBytes[6] &= 0x0f;
    randomBytes[6] |= 0x40;
    randomBytes[8] &= 0x3f;
    randomBytes[8] |= 0x80;
    long msb = 0;
    long lsb = 0;
    for (int i = 0; i < 8; i++) {
      msb = (msb << 8) | (randomBytes[i] & 0xff);
    }
    for (int i = 8; i < 16; i++) {
      lsb = (lsb << 8) | (randomBytes[i] & 0xff);
    }
    return new UUID(msb, lsb);
  }

}
