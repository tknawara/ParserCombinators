package edu.parsec.data.list;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import edu.parsec.typeclass.Monad;

/**
 * List case class. this class allows you to build you list
 *
 * @param <T> type of elements in the class
 * @author Tarek nawara
 */
public class Cons<T> implements IList<T> {

    private final T head;
    private final IList<T> tail;

    public Cons(T head, IList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public IList<T> filter(Predicate<T> p) {
        if (p.test(head)) {
            return new Cons<>(head, tail.filter(p));
        }
        return tail.filter(p);
    }

    @Override
    public IList<T> takeWhile(Predicate<T> p) {
        if (p.test(head)) {
            return new Cons<>(head, tail.takeWhile(p));
        }
        return new Empty<>();
    }

    @Override
    public IList<T> dropWhile(Predicate<T> p) {
        if (p.test(head)) {
            return tail.dropWhile(p);
        }
        return this;
    }

    @Override
    public IList<T> reverse() {
        IList<T> result = new Empty<>();
        for (T x : this) {
            result = result.addFront(x);
        }
        return result;
    }

    @Override
    public IList<T> append(IList<T> other) {
        return new Cons<>(head, tail.append(other));
    }

    @Override
    public T head() {
        return this.head;
    }

    @Override
    public IList<T> tail() {
        return this.tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public <U> U foldLeft(U zeroElement, BiFunction<U, T, U> f) {
        IList<T> remaining = this;
        U acc = zeroElement;
        while (remaining.nonEmpty()) {
            T x = remaining.head();
            acc = f.apply(acc, x);
            remaining = remaining.tail();
        }
        return acc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        IList<T> remaining = this;
        while (remaining.tail().nonEmpty()) {
            sb.append(remaining.head()).append(", ");
            remaining = remaining.tail();
        }
        sb.append(remaining.head()).append("]");
        return sb.toString();
    }

    class ConsIterator implements Iterator<T> {
        IList<T> itrList = Cons.this;

        @Override
        public boolean hasNext() {
            return itrList.nonEmpty();
        }

        @Override
        public T next() {
            T result = itrList.head();
            itrList = itrList.tail();
            return result;
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new ConsIterator();
    }

    @Override
    public <B> IList<B> map(Function<T, B> f) {
        return new Cons<>(f.apply(head), tail.map(f));
    }

    @Override
    public <B> IList<B> flatMap(Function<T, Monad<B, IList<?>>> f) {
        IList<B> rest = tail.flatMap(f);
        return ((IList<B>) f.apply(head)).append(rest);
    }

    @Override
    public <B> IList<B> pure(B value) {
        return new Cons<>(value, new Empty<>());
    }

    @Override
    public String mkString(String delimitar) {
        if (tail.isEmpty()) {
            return head.toString();
        }
        IList<T> current = this;
        StringBuilder sb = new StringBuilder();
        while (current.tail().nonEmpty()) {
            sb.append(current.head().toString());
            sb.append(delimitar);
            current = current.tail();
        }
        sb.append(current.head().toString());
        return sb.toString();
    }
}
