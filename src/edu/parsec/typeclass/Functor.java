package edu.parsec.typeclass;

import java.util.function.Function;

/**
 * Representation of a {@code functor} in java.
 *
 * @param <T> type parameter of the functor
 * @param <FClass> class of the functor
 */
public interface Functor<T, FClass extends Functor<?, FClass>> {

    /**
     * Map a function over the Functor class
     *
     * @param f function to map with
     * @return a new Functor with the mapped value
     */
    <B> Functor<B, FClass> map(Function<T, B> f);
}
