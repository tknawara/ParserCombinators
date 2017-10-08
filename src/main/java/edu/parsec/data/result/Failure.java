package edu.parsec.data.result;

import java.util.Objects;
import java.util.function.Function;

/**
 * Representing the failure case of the result
 * <p>
 * This is similar to {@code Left} in the {@code Either} monad
 * </p>
 *
 * @param <T> type of the result
 * @author Tarek
 */
public class Failure<T> implements Result<T> {

    public final String error;

    public Failure(final String error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public <U> Result<U> map(final Function<T, U> f) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Failure<?> failure = (Failure<?>) o;
        return Objects.equals(error, failure.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return "Failure: " + error;
    }
}
