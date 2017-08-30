package com.simplaex.dummies;

import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class DummiesStaticMethodsTest {

  @Test
  public void checkBuilder() {
    Assert.assertTrue(
        "build() should return an instance of DummiesBuilderImpl",
        Dummies.builder() instanceof DummiesBuilderImpl
    );
  }

  @Test
  public void checkGet() {
    Assert.assertTrue(
        "get() invoked twice should return the same singleton instance",
        Dummies.get() == Dummies.get()
    );
  }

  @Test
  public void checkForSeed() {
    Assert.assertTrue(
        "forSeed(long) invoked with the same seed should return Dummies with the same behavior",
        Dummies.forSeed(0).getRandom().nextLong() == Dummies.forSeed(0).getRandom().nextLong()
    );
  }

  @Test
  public void checkForSeed2() {
    Assert.assertTrue(
        "forSeed(long) invoked with the different seed should return Dummies with different behavior",
        Dummies.forSeed(1).getRandom().nextLong() != Dummies.forSeed(0).getRandom().nextLong()
    );
  }

  @Test
  public void checkForSeed3() {
    Assert.assertTrue(
        "forSeed(long) invoked with the same seed should return different instances",
        Dummies.forSeed(0).getRandom() != Dummies.forSeed(0)
    );
  }

  @Test
  public void checkSecure() {
    Assert.assertTrue(
        "secure() should return a Dummies with a SecureRandom generator",
        Dummies.secure().getRandom() instanceof SecureRandom
    );
  }

  @Test
  public void checkThreadLocalRandom() {
    Assert.assertTrue(
        "The default Dummies should have a ThreadLocalRandom generator",
        Dummies.get().getRandom() instanceof ThreadLocalRandom
    );
  }
}
