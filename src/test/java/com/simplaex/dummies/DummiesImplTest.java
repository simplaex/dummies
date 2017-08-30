package com.simplaex.dummies;

import com.simplaex.dummies.annotation.DummyValues;
import com.simplaex.dummies.generators.NameGenerator;
import lombok.Data;
import lombok.Value;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class DummiesImplTest {

  @Value
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

  public enum SomeEnum {
    E_ONE,
    E_TWO
  }

  @Test
  public void checkCreationOfEnum() {

    val anEnum = Dummies.get().create(SomeEnum.class);

    Assert.assertNotNull(anEnum);
    Assert.assertTrue(anEnum == SomeEnum.E_ONE || anEnum == SomeEnum.E_TWO);
  }

  @Value
  public class Nested {

    private JustAnotherPojo justAnotherPojo;

    private ValueClass valueClass;

    private SomeEnum someEnum;

  }

  @Test
  public void checkNestedCreation() {

    val nested = Dummies.get().create(Nested.class);

    Assert.assertNotNull(nested);

    val pojo = nested.getJustAnotherPojo();

    Assert.assertNotNull(pojo);
    Assert.assertNotNull(pojo.getId());
    Assert.assertEquals(4, pojo.getId().version());
    Assert.assertNotNull(pojo.getCreatedAt());
    Assert.assertNotNull(pojo.getUpdatedAt());

    val valueClass = nested.getValueClass();

    Assert.assertNotNull(valueClass);
    Assert.assertNotNull(valueClass.getId());
    Assert.assertEquals(4, valueClass.getId().version());
    Assert.assertNotNull(valueClass.getCreatedAt());
    Assert.assertNotNull(valueClass.getUpdatedAt());

    val anEnum = nested.getSomeEnum();

    Assert.assertNotNull(anEnum);
    Assert.assertTrue(anEnum == SomeEnum.E_ONE || anEnum == SomeEnum.E_TWO);
  }

  @Test
  public void checkUUIDFromString() {

    val uuid = Dummies.get().fromString(UUID.class, "AAB405CA-89BD-42D6-A9E1-0F101917FECC");

    Assert.assertEquals(
        UUID.fromString("AAB405CA-89BD-42D6-A9E1-0F101917FECC"),
        uuid
    );
  }

  @Test
  public void checkInstantFromString() {

    val instant = Dummies.get().fromString(Instant.class, "1987-08-14T17:35:00Z");

    Assert.assertEquals(
        Instant.ofEpochMilli(555960900000L),
        instant
    );
  }

  @Test
  public void checkNameCreation() {

    val dummies = Dummies.builder()
        .withGenerator(String.class, NameGenerator.class)
        .build();

    val someName = dummies.create(String.class);

    Assert.assertNotNull(someName);
    Assert.assertFalse(someName.isEmpty());

    val names = someName.split(" ");

    Assert.assertNotNull(names);
    Assert.assertEquals(2, names.length);
    Assert.assertNotNull(names[0]);
    Assert.assertNotNull(names[1]);
    Assert.assertTrue(names[0].length() >= 1);
    Assert.assertTrue(names[1].length() >= 1);

  }

  interface PrimitiveInt {
    int getValue();
  }

  @Value
  public static class PrimitiveIntValue implements PrimitiveInt {
    @DummyValues(intMin = 1)
    private int value;
  }

  @Data
  public static class PrimitiveIntPojo implements PrimitiveInt {
    @DummyValues(intMin = 1)
    private int value;
  }

  private void primitiveIntAssertions(final PrimitiveInt obj) {
    Assert.assertNotNull(obj);
    Assert.assertTrue(obj.getValue() + " should be > 0", obj.getValue() > 0);
  }

  @Test
  public void checkCreationOfValueWithPrimitiveIntProperty() {
    for (int i = 0; i < 20; i += 1) {
      val obj = Dummies.get().create(PrimitiveIntValue.class);
      primitiveIntAssertions(obj);
    }
  }

  @Test
  public void checkCreationOfPojoWithPrimitiveIntProperty() {
    for (int i = 0; i < 20; i += 1) {
      val obj = Dummies.get().create(PrimitiveIntPojo.class);
      primitiveIntAssertions(obj);
    }
  }


}
