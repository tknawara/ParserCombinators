package edu.parsec.typeclass;

import java.util.function.Function;

/**
 * Interface to represent the applicative functor.
 *
 * @param <T> type of the functor
 * @param <AClass>
 */
public interface Applicative<T, AClass extends Applicative<?, AClass>> extends Functor<T, AClass> {

	/**
	 * Lift a value of any type {@code B} inside an Applicative
	 * 
	 * @param value
	 *            the value to lift inside an applicative
	 * @return an Applicative of this value
	 */
	<B> Applicative<B, AClass> pure(B value);

	/**
	 * Apply a function wrapped inside a Applicative to the current monad the
	 * result is a new Applicative.
	 * <p>
	 * You may be familiar with the {@code <*>} infix operator from the
	 * Applicative because each monad is also applicative we included this
	 * function
	 * </p>
	 * 
	 * @param fM
	 *            function as a applicative
	 * @return a new monad that is the result of mapping the wrapped function
	 *         over the current applicative
	 */
	<C> Applicative<C, AClass> apply(Applicative<Function<T, C>, AClass> fM);
}
