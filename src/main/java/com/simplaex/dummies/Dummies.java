package com.simplaex.dummies;

import java.util.Random;
import java.util.function.Consumer;

public interface Dummies {

  /**
   * Creates a new instance of the class T.
   *
   * @param clazz The class instance of the type T.
   * @param <T>   The type T.
   * @return An instance of type T, or null if the instantiation failed for some reason.
   * @since 1.0.0
   */
  <T> T create(final Class<T> clazz);

  /**
   * Fills the properties of a JavaBean/POJO with getters/setters.
   *
   * @param instance The instance to be populated with dummy data.
   * @param <T>      The type of that instance.
   * @return The very same instance, but mutated with some nice and warm dummy data.
   * @since 1.0.0
   */
  <T> T fill(final T instance);

  /**
   * Tries to instantiates a class of a given type from a String representation.
   * <p>
   * If the class has a single parameter constructor that takes a string, then
   * that is used to create the class.
   * <p>
   * Otherwise if the class has a public static method that takes a single String
   * and returns the requested type then that is used to create the instance.
   * <p>
   * Otherwise if the class has a public static method that takes a single CharSequence
   * and returns the requested type then that is used to create the instance.
   * <p>
   * Otherwise if the class has a public static method that takes a single byte array
   * and returns the requested type then the given string is turned into a byte array
   * using UTF-8 as charset and that method is used to create the instance.
   * <p>
   * Otherwise null is returned.
   *
   * @param clazz       The class instance of the type T.
   * @param stringValue The string value which is used to create the class.
   * @param <T>         The type T.
   * @return An instance of type T, or null.
   * @since 1.0.0
   */
  <T> T fromString(final Class<T> clazz, final String stringValue);

  /**
   * Returns the random generator used by this instance of Dummies.
   * <p>
   * This method is important when writing a custom {@link Generator}.
   * Generators are supposed to use the random generator provided by Dummies,
   * making sure to generate random data from the same source which is
   * important for example for using Dummies with a predefined seed.
   * <p>
   * See {@link #forSeed(long)}
   *
   * @return The random generator used by this instance of Dummies.
   * @since 1.0.0
   */
  Random getRandom();


  /**
   * Creates a new Dummies instance that uses a random generator
   * initialized with the given seed.
   * <p>
   * Useful for repeatability.
   *
   * @param seed The seed to initialize the underlying random generator with.
   * @return A new instance of Dummies.
   * @since 1.0.0
   */
  static Dummies forSeed(final long seed) {
    return builder().withSeed(seed).build();
  }

  /**
   * Creates a new Dummies instance that uses a {@link java.security.SecureRandom}
   * to generate random data.
   *
   * @return An new instance of Dummies.
   * @since 1.0.0
   */
  static Dummies secure() {
    return builder().secure().build();
  }

  /**
   * Returns the default Dummies instance that uses a {@link java.util.concurrent.ThreadLocalRandom}
   * to generate random data.
   * <p>
   * This method returns the same Dummies instance every time.
   * Use it, except you need some fancy features such as your own
   * {@link Generator}s or a deterministic Dummies instance.
   *
   * @return An instance of Dummies.
   * @since 1.0.0
   */
  static Dummies get() {
    return DummiesSingleton.INSTANCE;
  }

  /**
   * Create a builder for creating a customized instance of Dummies.
   *
   * @return A new builder.
   * @since 1.0.0
   */
  static Builder builder() {
    return new DummiesBuilderImpl();
  }

  /**
   * A builder for creating customized instances of Dummies.
   * <p>
   * Builders are immutable and thread-safe and therefore reusable.
   * Every method on a builder creates a new builder.
   *
   * @since 1.0.0
   */
  interface Builder {

    Builder secure();

    Builder withSeed(final long seed);

    <T> Builder withGenerator(final Class<T> clazz, final Generator<T> generator);

    <T, G extends Generator<T>> Builder withGenerator(final Class<T> clazz, final Class<G> generator);

    /**
     * @param exceptionHandler A consumer function that will be invoked by
     *                         the Dummies instance created by the new builder
     *                         whenever an exception happens.
     * @return A new builder that will use the given exception handler.
     * @since 1.1.0
     */
    Builder withExceptionHandler(final Consumer<Exception> exceptionHandler);

    Dummies build();

  }
}
