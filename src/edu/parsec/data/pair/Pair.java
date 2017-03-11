package edu.parsec.data.pair;

import java.util.function.Function;

/**
 * Simple class to represents a {@code Tuple2}
 * 
 * @author Tarek
 *
 * @param <T>
 *            type of first element in the tuple
 * @param <U>
 *            type of second element in the tuple
 */
public class Pair<T, U> {

	public final T first;
	public final U second;

	public Pair(T first, U second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	public <B> Pair<B, U> mapFirst(Function<T, B> f) {
		return new Pair<>(f.apply(first), second);
	}

	@Override
	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}
}
