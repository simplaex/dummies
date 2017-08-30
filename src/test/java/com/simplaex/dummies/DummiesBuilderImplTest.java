package com.simplaex.dummies;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DummiesBuilderImplTest {

  @Test
  public void checkBuild() {
    Assert.assertTrue(
        "build() should instantiate a Dummies instance",
        new DummiesBuilderImpl().build() != null
    );
  }

  @Test
  public void checkWithGenerator() {
    val uuid = UUID.nameUUIDFromBytes("hello".getBytes(StandardCharsets.UTF_8));
    val dummies = new DummiesBuilderImpl()
        .withGenerator(UUID.class, () -> uuid)
        .build();

    Assert.assertEquals(
        uuid,
        dummies.create(UUID.class)
    );
  }

  @Test
  public void checkWithTwoGenerators() {
    val uuid = UUID.nameUUIDFromBytes("hello".getBytes(StandardCharsets.UTF_8));
    val dummies = new DummiesBuilderImpl()
        .withGenerator(UUID.class, () -> uuid)
        .withGenerator(String.class, () -> "123")
        .build();

    Assert.assertEquals(
        uuid,
        dummies.create(UUID.class)
    );
    Assert.assertEquals(
        "123",
        dummies.create(String.class)
    );
  }
}
