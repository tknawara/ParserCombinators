package edu.parsec.data.result;

import java.util.function.Function;

/**
 * Representing the failure case of the result
 * <p>
 * This is similar to {@code Left} in the {@code Either} monad
 * </p>
 * 
 * @author Tarek
 *
 * @param <T>
 *            type of the result
 */
public class Failure<T> implements Result<T> {

	public final String error;

	public Failure(String error) {
		this.error = error;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public <U> Result<U> map(Function<T, U> f) {
		return new Failure<>(error);
	}

	@Override
	public T get() {
		throw new RuntimeException("Trying to get value from failure");
	}

	@Override
	public String getErrorMessage() {
		return error;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Failure other = (Failure) obj;
		if (error == null) {
			if (other.error != null)
				return false;
		} else if (!error.equals(other.error))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Failure: " + error;
	}
}
