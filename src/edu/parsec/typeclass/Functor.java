package edu.parsec.typeclass;

import java.util.function.Function;

public interface Functor<T, FClass extends Functor<?, FClass>> {

	/**
	 * Map a function over the Functor class
	 * 
	 * @param f
	 *            function to map with
	 * @return a new Functor with the mapped value
	 */
	public <B> Functor<B, FClass> map(Function<T, B> f);
}
