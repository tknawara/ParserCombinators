package edu.parsec.data.list;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import edu.parsec.typeclass.Monad;

public class Empty<T> implements IList<T> {

	@Override
	public IList<T> filter(Predicate<T> p) {
		return this;
	}

	@Override
	public IList<T> takeWhile(Predicate<T> p) {
		return this;
	}

	@Override
	public IList<T> dropWhile(Predicate<T> p) {
		return this;
	}
	
	@Override
	public IList<T> reverse() {
		return this;
	}

	@Override
	public IList<T> append(IList<T> other) {
		return other;
	}

	@Override
	public T head() {
		throw new NoSuchElementException();
	}

	@Override
	public IList<T> tail() {
		throw new NoSuchElementException();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public <U> U foldLeft(U zeroElement, BiFunction<U, T, U> f) {
		return zeroElement;
	}

	@Override
	public String toString() {
		return "[]";
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> result = new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public T next() {
				return null;
			}
		};
		
		return result;
	}

	@Override
	public <B> IList<B> pure(B value) {
		return new Cons<>(value, new Empty<>());
	}

	@Override
	public <B> IList<B> map(Function<T, B> f) {
		return new Empty<>();
	}

	@Override
	public <C> IList<C> apply(Monad<Function<T, C>, IList<?>> fM) {
		return new Empty<>();
	}

	@Override
	public <B> IList<B> flatMap(Function<T, Monad<B, IList<?>>> f) {
		return new Empty<>();
	}

	@Override
	public String mkString(String delimitar) {
		return "";
	}
}