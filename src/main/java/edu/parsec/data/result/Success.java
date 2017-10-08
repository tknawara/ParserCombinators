package edu.parsec.data.result;

import java.util.Objects;
import java.util.function.Function;

/**
 * A Representation of the success result.
 * <p>
 * It is similar to the {@code Right} of the {@code Either} monad
 * </p>
 *
 * @param <T> type of the result
 * @author Tarek
 */
public class Success<T> implements Result<T> {

    private final T value;

    public Success(final T value) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Success<?> success = (Success<?>) o;
        return Objects.equals(value, success.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Success: " + value.toString();
    }
}
