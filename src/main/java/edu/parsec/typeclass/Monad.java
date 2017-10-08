package edu.parsec.typeclass;

import java.util.function.BiFunction;
import java.util.function.Function;

import edu.parsec.data.list.Cons;
import edu.parsec.data.list.Empty;
import edu.parsec.data.list.IList;
import edu.parsec.data.unit.Unit;

/**
 * Interface representing the {@code Monad} in java.
 *
 * @param <T>      type argument for the monad
 * @param <MClass> type of the monadic class
 * @author Tarek Nawara
 */
public interface Monad<T, MClass extends Monad<?, MClass>> {

    /**
     * Lift a value of any type {@code B} inside the monad
     *
     * @param value the value to lift inside the monad
     * @return a monad of this value
     */
    <B> Monad<B, MClass> pure(final B value);

    /**
     * Map a non-monadic function over the monadic value
     *
     * @param f function to map with
     * @return a new monad with the mapped value
     */
    <B> Monad<B, MClass> map(final Function<T, B> f);

    /**
     * FlatMap a function that takes the value inside the monad and returns a
     * new monad
     * <p>
     * You may be familiar with the name {@code Bind} for this function but
     * flatMap is the <tt>Scala</tt> convention
     * </p>
     *
     * @param f function to use in the mapping
     * @return a new monad produced by the function
     */
    <B> Monad<B, MClass> flatMap(final Function<T, Monad<B, MClass>> f);

    /**
     * Sequentially compose two actions, discarding any value produced by the
     * first, like sequencing operators (such as the semicolon) in imperative
     * languages.
     *
     * @param other the action which its result will be returned
     * @return action that will run both actions and return the result of the
     * {@code other} action
     */
    default <B> Monad<B, MClass> then(final Monad<B, MClass> other) {
        return this.flatMap(f -> other);
    }

    /**
     * Apply a function wrapped inside a monad to the current monad the result
     * is a new monad.
     * <p>
     * You may be familiar with the {@code <*>} infix operator from the
     * Applicative because each monad is also applicative we included this
     * function
     * </p>
     *
     * @param fM function as a monad
     * @return a new monad that is the result of mapping the wrapped function
     * over the current monad
     */
    default <C> Monad<C, MClass> apply(final Monad<Function<T, C>, MClass> fM) {
        return fM.flatMap(this::map);
    }

    /**
     * Lift a function to monadic world. then apply it to the current instance
     * as first argument then to paramTwo
     *
     * @param f        curried function that takes to values and return a new value
     * @param paramTwo the second parameter to apply. the first one is the instance
     *                 itself
     * @return the result of the function lifted to the monadic world
     */
    default <B, C> Monad<C, MClass> liftM2(final Function<T, Function<B, C>> f,
                                           final Monad<B, MClass> paramTwo) {
        final Monad<Function<T, Function<B, C>>, MClass> funcOne = pure(f);
        final Monad<Function<B, C>, MClass> funcTwo = this.apply(funcOne);
        return paramTwo.apply(funcTwo);
    }

    /**
     * Lift a BiFunction to monadic world. then apply it to the current instance
     * as first argument then to paramTwo
     *
     * @param f        Uncured function that takes to values and return a new value
     * @param paramTwo the second parameter to apply. the first one is the instance
     *                 itself
     * @return the result of the function lifted to the monadic world
     */
    default <B, C> Monad<C, MClass> liftM2(final BiFunction<T, B, C> f, Monad<B, MClass> paramTwo) {
        Function<T, Function<B, C>> curried = (t -> b -> f.apply(t, b));
        return liftM2(curried, paramTwo);
    }

    /**
     * Evaluate each monadic action in the structure from left to right, and
     * collect the results.
     * <p>
     * You can imagine this method a way to flip the types. it takes a IList of
     * type {@code Monad<B>} and returns a monad of type {@code Monad<IList<B>>}
     * </p>
     *
     * @param mList list of monadic instances of type {@code B}
     * @return a monad of a list of type {@code B}
     */
    default <B> Monad<IList<B>, MClass> sequence(final IList<? extends Monad<B, MClass>> mList) {
        Monad<IList<B>, MClass> result = pure(new Empty<>());
        Function<B, Function<IList<B>, IList<B>>> consCurried = (elem -> l -> new Cons<>(elem, l));
        for (final Monad<B, MClass> parser : mList.reverse()) {
            result = parser.liftM2(consCurried, result);
        }
        return result;
    }

    /**
     * Map each element of a structure to a monadic action, evaluate these
     * actions from left to right, and collect the results. there exist a
     * version that ignore the return value
     *
     * @param f    this function takes a value of type {@code A} and returns a
     *             monad of {@code B}
     * @param list list of values of type {@code A}
     * @return m t b
     */
    default <A, B> Monad<IList<B>, MClass> mapM(final Function<A, Monad<B, MClass>> f,
                                                final IList<A> list) {
        final IList<Monad<B, MClass>> mList = list.map(f);
        return this.sequence(mList);
    }

    /**
     * Map each element of a structure to a monadic action, evaluate these
     * actions from left to right, and collect the results. this version simply
     * throws the result away. this version is very useful if the function
     * {@code f} has a side effect
     *
     * @param f    function to map over the list
     * @param list list of elements of generic type {@code A}
     * @return Unit
     */
    default <A, B> Unit mapM_(final Function<A, Monad<B, MClass>> f, final IList<A> list) {
        final IList<Monad<B, MClass>> mList = list.map(f);
        this.sequence(mList);
        return Unit.get();
    }

    /**
     * This function is same as {@code mapM} but with flipped arguments
     *
     * @param list list of elements of type {@code A}
     * @param f    a transformation function from {@code A} to {@code Monad<B>}
     * @return a monad of the list resulting from evaluating each monadic action
     * and collect the results
     */

    default <A, B> Monad<IList<B>, MClass> forM(final IList<A> list,
                                                final Function<A, Monad<B, MClass>> f) {
        return this.mapM(f, list);
    }

    /**
     * This function is same as {@code mapM} but with flipped arguments
     *
     * @param list list of elements of type {@code A}
     * @param f    a transformation function from {@code A} to {@code Monad<B>}
     * @return a monad of the list resulting from evaluating each monadic action
     * and collect the results
     */
    default <A, B> Unit forM_(final IList<A> list, final Function<A, Monad<B, MClass>> f) {
        return this.mapM_(f, list);
    }
}
