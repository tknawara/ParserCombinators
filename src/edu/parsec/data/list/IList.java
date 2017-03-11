package edu.parsec.data.list;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import edu.parsec.typeclass.Monad;

/**
 * List implementation based on Lisp lists
 * 
 * @author Tarek
 * @param <T>
 *            type of elements inside the list
 */
public interface IList<T> extends Iterable<T>, Monad<T, IList<?>> {

	/**
	 * Adds an element to the list, this list is immutable so this method
	 * returns a new list with the value added to it
	 * 
	 * @param elem
	 *            element to add
	 * @return a new list that contains this element at the beginning
	 */
	default public IList<T> addFront(T elem) {
		return new Cons<>(elem, this);
	}

	/**
	 * Returns a new List containing only the elements satisfying the given
	 * predicate
	 * 
	 * @param p
	 *            predicate to test the elements with
	 * @return a new list of elements satisfying the predicate
	 */
	public IList<T> filter(Predicate<T> p);

	/**
	 * Returns a new list contains the first elements in the original list
	 * satisfying some predicate
	 * 
	 * @param p
	 *            predicate to test the elements with
	 * @return a new list contains elements satisfying the predicate
	 */
	public IList<T> takeWhile(Predicate<T> p);

	/**
	 * Drop all the elements in the beginning of the list satisfying some
	 * predicate
	 * 
	 * @param p
	 *            the predicate to test the elements with
	 * @return the original list without the first elements satisfying some
	 *         predicate
	 */
	public IList<T> dropWhile(Predicate<T> p);

	/**
	 * Return a copy of the current list with elements in reverse order
	 * <p>
	 * The current instance is not affected by invoking this method
	 * </p>
	 * 
	 * @return a copy of the current list with elements in reverse order
	 */
	public IList<T> reverse();

	/**
	 * Append on list to the current list
	 * 
	 * @param other
	 *            other list to append
	 * @return new list consist from the two lists
	 */
	public IList<T> append(IList<T> other);

	/**
	 * Join the elements of the list given the {@code delimiter} to join with.
	 * 
	 * @param delimiter
	 *            used to join the elements
	 * @return a string of the list's elements joined by the delimiter
	 */
	public String mkString(String delimiter);

	/**
	 * Builds a list from any traversable sequence
	 * 
	 * @param traversable
	 *            the sequence to build from
	 * @return a list of the sequence
	 */
	public static <T> IList<T> buildFrom(Iterable<T> traversable) {
		return IList.buildFrom(traversable.iterator());
	}

	/**
	 * Builds a list from any iterator
	 * 
	 * @param itr
	 *            the iterator to build the list with.
	 * @return a list of elements of the iterator
	 */
	public static <T> IList<T> buildFrom(Iterator<T> itr) {
		if (itr.hasNext()) {
			return new Cons<>(itr.next(), buildFrom(itr));
		}
		return new Empty<>();
	}

	/**
	 * Builds a list of characters from a string. because java doesn't consider
	 * strings as traversable sequence
	 * 
	 * @param s
	 *            string to build the list from
	 * @return a list consist of the characters of the string s.
	 */
	public static IList<Character> buildFrom(String s) {
		IList<Character> result = new Empty<>();
		for (int i = s.length() - 1; i >= 0; --i) {
			result = result.addFront(s.charAt(i));
		}
		return result;
	}

	/**
	 * Builds a list from array elements of any type {@code T}
	 * <p>
	 * This function works on generic arrays only and doesn't work on primitives
	 * because java distinguish between primitive types and generic types
	 * </p>
	 * 
	 * @param a
	 *            the array to build from
	 * @return list consist of the elements of the array
	 */
	public static <T> IList<T> buildFrom(T[] a) {
		IList<T> result = new Empty<>();
		for (int i = a.length - 1; i >= 0; --i) {
			result = new Cons<>(a[i], result);
		}
		return result;
	}

	/**
	 * Apply the function successively over the list from left to right using
	 * {@code zeroElement} as starting element
	 * 
	 * @param zeroElement
	 *            element to start with.
	 * @param f
	 *            the {@code BiFunction} to use in the function application.
	 * @return single value after the successive applications of the function
	 */
	public <U> U foldLeft(U zeroElement, BiFunction<U, T, U> f);

	/**
	 * Tests wither the list is empty or not
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty();

	/**
	 * Tests wither the list is not empty or not
	 * 
	 * @return true if not empty, false otherwise
	 */
	default public boolean nonEmpty() {
		return !isEmpty();
	}

	/**
	 * Get the first element of the list. if the list is empty an
	 * {@code Exception} is thrown
	 * 
	 * @return the first element of the list if the list is not empty, or
	 *         {@code Exception} otherwise
	 */
	public T head();

	/**
	 * Get the rest of the elements of the list leaving the first element. if
	 * the list is empty an {@code Exception} will be thrown
	 * 
	 * @return all the elements of the list leaving the first one
	 */
	public IList<T> tail();

	/*
	 * Bridge method to make Monad#pure type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#pure(java.lang.Object)
	 */
	@Override
	public <B> IList<B> pure(B value);

	/*
	 * Bridge method to make Monad#map type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#map(java.util.function.Function)
	 */
	@Override
	public <B> IList<B> map(Function<T, B> f);

	/*
	 * Bridge method to make Monad#flatMap type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#flatMap(java.util.function.Function)
	 */
	@Override
	public <B> IList<B> flatMap(Function<T, Monad<B, IList<?>>> f);

	/*
	 * Bridge method to make Monad#apply type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#apply(edu.parsec.typeclass.Monad)
	 */
	@Override
	default public <C> IList<C> apply(Monad<Function<T, C>, IList<?>> fM) {
		return (IList<C>) Monad.super.apply(fM);
	}

	/*
	 * Bridge method to make Monad#liftM2 type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#liftM2(java.util.function.Function,
	 * edu.parsec.typeclass.Monad)
	 */
	@Override
	default public <B, C> IList<C> liftM2(final Function<T, Function<B, C>> f, final Monad<B, IList<?>> paramTwo) {
		return (IList<C>) Monad.super.liftM2(f, paramTwo);
	}

	/*
	 * Bridge method to make Monad#sequence type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#sequence(edu.parsec.data.list.IList)
	 */
	@Override
	default public <B> IList<IList<B>> sequence(final IList<? extends Monad<B, IList<?>>> mList) {
		return (IList<IList<B>>) Monad.super.sequence(mList);
	}

	/*
	 * Bridge method to make Monad#mapM type aware of {@code IList<T>}
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#mapM(java.util.function.Function,
	 * edu.parsec.data.list.IList)
	 */
	@Override
	default public <A, B> IList<IList<B>> mapM(final Function<A, Monad<B, IList<?>>> f, final IList<A> list) {
		return (IList<IList<B>>) Monad.super.mapM(f, list);
	}

	/*
	 * Bridge method to make Monad#forM type aware of {@code IList<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#forM(edu.parsec.data.list.IList,
	 * java.util.function.Function)
	 */
	@Override
	default public <A, B> IList<IList<B>> forM(final IList<A> list, final Function<A, Monad<B, IList<?>>> f) {
		return (IList<IList<B>>) Monad.super.forM(list, f);
	}
}