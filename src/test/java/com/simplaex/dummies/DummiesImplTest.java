package com.simplaex.dummies;

import lombok.Data;
import lombok.Value;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

public class DummiesImplTest {

  @Value
  @SuppressWarnings("WeakerAccess")
  public static class ValueClass {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

  }

  @Test
  public void checkCreationOfValueClass() {

    val valueClass = Dummies.get().create(ValueClass.class);

    Assert.assertNotNull(valueClass);
    Assert.assertNotNull(valueClass.getId());
    Assert.assertEquals(4, valueClass.getId().version());
    Assert.assertNotNull(valueClass.getCreatedAt());
    Assert.assertNotNull(valueClass.getUpdatedAt());
  }

  @Data
  @SuppressWarnings("WeakerAccess")
  public static class JustAnotherPojo {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

  }

  @Test
  public void checkFillOfPojo() {

    val pojo = new JustAnotherPojo();

    Dummies.get().fill(pojo);

    Assert.assertNotNull(pojo);
    Assert.assertNotNull(pojo.getId());
    Assert.assertEquals(4, pojo.getId().version());
    Assert.assertNotNull(pojo.getCreatedAt());
    Assert.assertNotNull(pojo.getUpdatedAt());
  }

  @Test
  public void checkCreationOfPojo() {

    val pojo = Dummies.get().create(JustAnotherPojo.class);

    Assert.assertNotNull(pojo);
    Assert.assertNotNull(pojo.getId());
    Assert.assertEquals(4, pojo.getId().version());
    Assert.assertNotNull(pojo.getCreatedAt());
    Assert.assertNotNull(pojo.getUpdatedAt());
  }

}
