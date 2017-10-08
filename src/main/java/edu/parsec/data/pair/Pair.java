package edu.parsec.data.pair;

import java.util.Objects;

/**
 * Simple class to represents a {@code Tuple2}
 *
 * @param <T> type of first element in the tuple
 * @param <U> type of second element in the tuple
 * @author Tarek
 */
public class Pair<T, U> {

    public final T first;
    public final U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
