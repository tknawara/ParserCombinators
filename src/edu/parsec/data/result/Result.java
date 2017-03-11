package edu.parsec.data.result;

import java.util.function.Function;

/**
 * Representation of the result of a computation that might fail
 * <p>
 * This is similar to the {@code Either} monad
 * </p>
 * 
 * @author Tarek
 *
 * @param <T>
 *            type of the value of the computation
 */
public interface Result<T> {

	/**
	 * Tests whether the current result is success or failure
	 * 
	 * @return true if success, false otherwise
	 */
	public boolean isSuccess();

	/**
	 * Map a computation over the result, if the result is success the mapping
	 * will happen, otherwise it will remain as a failure.
	 * 
	 * @param f
	 *            function to map with
	 * @return the mapped result
	 */
	public <U> Result<U> map(Function<T, U> f);

	/**
	 * Trying to get the value wrapped inside the result
	 * <p>
	 * If the result is failure it will throw a {@code RunTimeException}
	 * </p>
	 * 
	 * @return the value wrapped inside the monad
	 */
	public T get();

	default boolean isFailure() {
		return !isSuccess();
	}

	/**
	 * Trying to get the error message from the result. if the result is failure
	 * it will return the error message else it will return empty string
	 * 
	 * @return string representing the error message
	 */
	default public String getErrorMessage() {
		return "";
	}
}