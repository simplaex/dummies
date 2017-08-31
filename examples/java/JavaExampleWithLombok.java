import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.annotation.DummyValues;
import com.simplaex.dummies.generators.CountryNameGenerator;
import com.simplaex.dummies.generators.NameGenerator;
import lombok.Data;
import lombok.Value;
import lombok.val;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class JavaExampleWithLombok {

  public static void main(final String... args) {

    val record1 = Dummies.get().create(Record.class);
    val record2 = Dummies.get().fill(new Record());

    val user = Dummies.get().create(User.class);

    System.out.println(record1);
    System.out.println(record2);
    System.out.println(user);

  }

  @Data
  public static class Record {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

  }

  @Value
  public static class User {

    @DummyValues(generator = NameGenerator.class)
    private final String name;

    private final Address currentAddress;

    @DummyValues(minLength = 0, maxLength = 5)
    private final List<Address> formerAddresses;
  }

  @Value
  public static class Address {

    @DummyValues({"Main Street", "Market Street", "Maple Street"})
    private final String street;

    @DummyValues({"New Amsterdam", "New Berlin", "New York"})
    private final String city;

    @DummyValues(generator = CountryNameGenerator.class)
    private final String country;
  }

}

