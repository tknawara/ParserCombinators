package edu.parsec.data.result;

import java.util.function.Function;

/**
 * A Representation of the success result.
 * <p>
 * It is similar to the {@code Right} of the {@code Either} monad
 * </p>
 * 
 * @author Tarek
 *
 * @param <T>
 *            type of the result
 */
public class Success<T> implements Result<T> {

	private final T value;

	public Success(T value) {
		this.value = value;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public <U> Result<U> map(Function<T, U> f) {
		return new Success<>(f.apply(value));
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Success other = (Success) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Success: " + value.toString();
	}
}
