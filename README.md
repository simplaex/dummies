# easy dummies for Java/Scala

## Setup

### Java/Maven

    <dependency>
      <groupId>com.simplaex</groupId>
      <artifactId>dummies</artifactId>
      <version>1.0.0</version>
    </dependency>

### Scala/SBT

    libraryDependencies += "com.simplaex" % "dummies" % "1.0.0"

## Examples

### Java/Lombok

    public class JavaExampleWithLombok {
    
      public static void main(final String... args) {
    
        val record1 = Dummies.get().create(Record.class);
        val record2 = Dummies.get().fill(new Record());
    
        val user = Dummies.get().create(User.class);
    
        System.out.println(record1);
        // -> JavaExample.Record(
        //      id=c235b645-cdb6-4d96-9048-fac93813ce36,
        //      createdAt=+271952187-10-06T20:59:55.612Z,
        //      updatedAt=+14800123-06-14T17:07:53.312Z)

        System.out.println(record2);
        // -> JavaExample.Record(
        //      id=b7fe44b8-f90d-41c3-8406-9276db0a406e,
        //      createdAt=+218282276-11-01T22:34:26.961Z,
        //      updatedAt=+153555425-04-24T17:03:03.930Z)

        System.out.println(user);
        // -> JavaExample.User(
        //      name=Julieta Vallon,
        //      currentAddress=JavaExample.Address(
        //        street=Main Street,
        //        city=New York,
        //        country=Thailand),
        //      formerAddresses=[
        //        JavaExample.Address(
        //          street=Maple Street,
        //          city=New York,
        //          country=Turkmenistan)])
    
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

### Scala

    object ScalaExample {
    
      def main(args: Array[String]): Unit = {
      
        val record = Dummies.get().create(classOf[Record])
        println(record)
        // -> Record(a2a64f26-ed99-4265-8b57-f668099339c7,+127329170-07-19T19:33:44.946Z,+68490793-08-27T22:13:40.674Z,CZ)

      }
    
      final case class Record(
        id: UUID,
        createdAt: Instant,
        updatedAt: Instant,
        @(DummyValues @field)(generator = classOf[CountryCodeGenerator])
        countryOfOrigin: String
      )

    }