import com.simplaex.dummies.Dummies;
import com.simplaex.dummies.annotation.DummyValues;
import com.simplaex.dummies.generators.CountryNameGenerator;
import com.simplaex.dummies.generators.NameGenerator;
import lombok.val;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class JavaExample {

  public static void main(final String... args) {

    final Record record1 = Dummies.get().create(Record.class);
    final Record record2 = Dummies.get().fill(new Record());

    final User user = Dummies.get().create(User.class);

    System.out.println(record1);
    System.out.println(record2);
    System.out.println(user);

  }

  public static class Record {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    public UUID getId() {
      return this.id;
    }

    public Instant getCreatedAt() {
      return this.createdAt;
    }

    public Instant getUpdatedAt() {
      return this.updatedAt;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public void setCreatedAt(Instant createdAt) {
      this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
      this.updatedAt = updatedAt;
    }

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Record)) return false;
      final Record other = (Record) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$id = this.getId();
      final Object other$id = other.getId();
      if (this$id == null ? other$id != null : !this$id.equals(other$id))
        return false;
      final Object this$createdAt = this.getCreatedAt();
      final Object other$createdAt = other.getCreatedAt();
      if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt))
        return false;
      final Object this$updatedAt = this.getUpdatedAt();
      final Object other$updatedAt = other.getUpdatedAt();
      if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt))
        return false;
      return true;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $id = this.getId();
      result = result * PRIME + ($id == null ? 43 : $id.hashCode());
      final Object $createdAt = this.getCreatedAt();
      result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
      final Object $updatedAt = this.getUpdatedAt();
      result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
      return result;
    }

    protected boolean canEqual(Object other) {
      return other instanceof Record;
    }

    public String toString() {
      return "JavaExample.Record(id=" + this.getId() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }
  }

  public static class User {

    @DummyValues(generator = NameGenerator.class)
    private final String name;

    private final Address currentAddress;

    @DummyValues(minLength = 0, maxLength = 5)
    private final List<Address> formerAddresses;

    @java.beans.ConstructorProperties({"name", "currentAddress", "formerAddresses"})
    public User(String name, Address currentAddress, List<Address> formerAddresses) {
      this.name = name;
      this.currentAddress = currentAddress;
      this.formerAddresses = formerAddresses;
    }

    public String getName() {
      return this.name;
    }

    public Address getCurrentAddress() {
      return this.currentAddress;
    }

    public List<Address> getFormerAddresses() {
      return this.formerAddresses;
    }

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof User)) return false;
      final User other = (User) o;
      final Object this$name = this.getName();
      final Object other$name = other.getName();
      if (this$name == null ? other$name != null : !this$name.equals(other$name))
        return false;
      final Object this$currentAddress = this.getCurrentAddress();
      final Object other$currentAddress = other.getCurrentAddress();
      if (this$currentAddress == null ? other$currentAddress != null : !this$currentAddress.equals(other$currentAddress))
        return false;
      final Object this$formerAddresses = this.getFormerAddresses();
      final Object other$formerAddresses = other.getFormerAddresses();
      if (this$formerAddresses == null ? other$formerAddresses != null : !this$formerAddresses.equals(other$formerAddresses))
        return false;
      return true;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $name = this.getName();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      final Object $currentAddress = this.getCurrentAddress();
      result = result * PRIME + ($currentAddress == null ? 43 : $currentAddress.hashCode());
      final Object $formerAddresses = this.getFormerAddresses();
      result = result * PRIME + ($formerAddresses == null ? 43 : $formerAddresses.hashCode());
      return result;
    }

    public String toString() {
      return "JavaExample.User(name=" + this.getName() + ", currentAddress=" + this.getCurrentAddress() + ", formerAddresses=" + this.getFormerAddresses() + ")";
    }
  }

  public static class Address {

    @DummyValues({"Main Street", "Market Street", "Maple Street"})
    private final String street;

    @DummyValues({"New Amsterdam", "New Berlin", "New York"})
    private final String city;

    @DummyValues(generator = CountryNameGenerator.class)
    private final String country;

    @java.beans.ConstructorProperties({"street", "city", "country"})
    public Address(String street, String city, String country) {
      this.street = street;
      this.city = city;
      this.country = country;
    }

    public String getStreet() {
      return this.street;
    }

    public String getCity() {
      return this.city;
    }

    public String getCountry() {
      return this.country;
    }

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Address)) return false;
      final Address other = (Address) o;
      final Object this$street = this.getStreet();
      final Object other$street = other.getStreet();
      if (this$street == null ? other$street != null : !this$street.equals(other$street))
        return false;
      final Object this$city = this.getCity();
      final Object other$city = other.getCity();
      if (this$city == null ? other$city != null : !this$city.equals(other$city))
        return false;
      final Object this$country = this.getCountry();
      final Object other$country = other.getCountry();
      if (this$country == null ? other$country != null : !this$country.equals(other$country))
        return false;
      return true;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $street = this.getStreet();
      result = result * PRIME + ($street == null ? 43 : $street.hashCode());
      final Object $city = this.getCity();
      result = result * PRIME + ($city == null ? 43 : $city.hashCode());
      final Object $country = this.getCountry();
      result = result * PRIME + ($country == null ? 43 : $country.hashCode());
      return result;
    }

    public String toString() {
      return "JavaExample.Address(street=" + this.getStreet() + ", city=" + this.getCity() + ", country=" + this.getCountry() + ")";
    }
  }

}

