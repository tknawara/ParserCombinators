package edu.parsec.parser.imp;

import java.util.function.BiFunction;
import java.util.function.Function;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Failure;
import edu.parsec.data.result.Result;
import edu.parsec.data.result.Success;
import edu.parsec.parser.combinators.ParserByName;
import edu.parsec.typeclass.Monad;

/**
 * This is implementation of a parser combinators.
 * <p>
 * The parser type is simply a function that when run on a given input
 * characters will return <code>success of pair</code>. if it managed to parse
 * the input characters, {@code first }of the pair is {@code T}the object
 * created from the parsed characters, the <code>second</code> of the pair is
 * the remaining characters. will return <code>failure of error message</code>
 * </p>
 * 
 * @author Tarek
 *
 * @param <T>
 *            the object will the parser return if it managed to parse the input
 *            characters
 */
public class Parser<T> implements Monad<T, Parser<?>> {

	private final Function<IList<Character>, Result<Pair<T, IList<Character>>>> parserFunc;

	/**
	 * Construct a parser object given a function that takes a stream of
	 * characters and return an object of type <code>T</code> if success and
	 * failure of error message otherwise
	 * 
	 * @param parserFunc
	 *            the function used in the construction
	 */
	public Parser(Function<IList<Character>, Result<Pair<T, IList<Character>>>> parserFunc) {
		this.parserFunc = parserFunc;
	}

	/**
	 * Run the parser on the given input characters.
	 * 
	 * @param inputChars
	 *            the stream of characters that we want to parse.
	 * @return if success, a pair of {@code T} and {@code IList<Character>}
	 *         otherwise a failure of error message
	 */
	public Result<Pair<T, IList<Character>>> run(IList<Character> inputChars) {
		return this.parserFunc.apply(inputChars);
	}

	public Result<Pair<T, IList<Character>>> run(String inputChars) {
		IList<Character> chars = IList.buildFrom(inputChars);
		return this.run(chars);
	}

	/**
	 * ORing two parser together.
	 * <p>
	 * If the first parser succeed we return its result. otherwise we return the
	 * result of the second parser
	 * </p>
	 * 
	 * @param other
	 *            other parser to or with.
	 * @return the result of the first parser if it succeed, otherwise the
	 *         result of the second parser.
	 */
	public <U extends T> Parser<T> or(ParserByName<U> other) {
		Function<IList<Character>, Result<Pair<T, IList<Character>>>> innerFunc = inputChars -> {
			Result<Pair<T, IList<Character>>> outer = parserFunc.apply(inputChars);
			if (outer.isFailure()) {
				Result<Pair<U, IList<Character>>> otherResult = other.get().run(inputChars);
				if (otherResult.isFailure()) {
					return new Failure<>(otherResult.getErrorMessage());
				} else {
					Pair<U, IList<Character>> otherPair = otherResult.get();
					T result = otherPair.first;
					return new Success<>(new Pair<>(result, otherPair.second));
				}
			}
			return outer;
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * This function is used to and two parser together.
	 * <p>
	 * for this function to return a success the two parser must both succeed
	 * and the result is a pair containing there two results
	 * </p>
	 * 
	 * @param other
	 *            the other parser to and with
	 * @return a parser that when succeed will return a pair of both the
	 *         parsers' result
	 */
	public <U> Parser<Pair<T, U>> and(ParserByName<U> other) {
		BiFunction<T, U, Pair<T, U>> f = (result1, result2) -> new Pair<>(result1, result2);
		return this.liftM2(f, other.get());
	}

	/**
	 * Run the current parser and the other parser then return the result of the
	 * current one
	 * 
	 * @param other
	 *            other parser to run
	 * @return a parser that when run will run both parser but throw the result
	 *         of the other
	 */
	public <U> Parser<T> skip(Parser<U> other) {
		BiFunction<T, U, T> f = (result1, result2) -> result1;
		return this.liftM2(f, other);
	}

	@Override
	public <B> Parser<B> pure(B result) {
		Function<IList<Character>, Result<Pair<B, IList<Character>>>> innerFunc = inputChars -> {
			Pair<B, IList<Character>> parserResult = new Pair<>(result, inputChars);
			return new Success<>(parserResult);
		};
		return new Parser<>(innerFunc);
	}

	@Override
	public <B> Parser<B> map(Function<T, B> f) {
		Function<IList<Character>, Result<Pair<B, IList<Character>>>> innerFunc = inputChars -> {
			Result<Pair<T, IList<Character>>> outer = parserFunc.apply(inputChars);
			if (outer.isFailure()) {
				return new Failure<>(outer.getErrorMessage());
			}
			Success<Pair<T, IList<Character>>> success = (Success<Pair<T, IList<Character>>>) outer;
			return success.map(x -> new Pair<>(f.apply(x.first), x.second));
		};
		return new Parser<>(innerFunc);
	}

	@Override
	public <B> Parser<B> flatMap(Function<T, Monad<B, Parser<?>>> f) {
		Function<IList<Character>, Result<Pair<B, IList<Character>>>> innerFunc = inputChars -> {
			Result<Pair<T, IList<Character>>> outer = parserFunc.apply(inputChars);
			if (outer.isFailure()) {
				return new Failure<>(outer.getErrorMessage());
			}
			Success<Pair<T, IList<Character>>> success = (Success<Pair<T, IList<Character>>>) outer;
			T result = success.get().first;
			IList<Character> remaining = success.get().second;
			return ((Parser<B>) f.apply(result)).run(remaining);
		};
		return new Parser<>(innerFunc);
	}

	/*
	 * Bridge method to make Monad#sequence type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#sequence(edu.parsec.data.list.IList)
	 */
	@Override
	public <B> Parser<IList<B>> sequence(IList<? extends Monad<B, Parser<?>>> mList) {
		return (Parser<IList<B>>) Monad.super.sequence(mList);
	}

	/*
	 * Bridge method to make Monad#then type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#then(edu.parsec.typeclass.Monad)
	 */
	@Override
	public <B> Parser<B> then(Monad<B, Parser<?>> other) {
		return (Parser<B>) Monad.super.then(other);
	}

	/*
	 * Bridge method to make Monad#liftM2 type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#liftM2(java.util.function.BiFunction,
	 * edu.parsec.typeclass.Monad)
	 */
	@Override
	public <B, C> Parser<C> liftM2(BiFunction<T, B, C> f, Monad<B, Parser<?>> paramTwo) {
		return (Parser<C>) Monad.super.liftM2(f, paramTwo);
	}

	/*
	 * Bridge method to make Monad#apply type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#apply(edu.parsec.typeclass.Monad)
	 */
	@Override
	public <C> Parser<C> apply(Monad<Function<T, C>, Parser<?>> fM) {
		return (Parser<C>) Monad.super.apply(fM);
	}

	/*
	 * Bridge method to make Monad#mapM type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#mapM(java.util.function.Function,
	 * edu.parsec.data.list.IList)
	 */
	@Override
	public <A, B> Parser<IList<B>> mapM(Function<A, Monad<B, Parser<?>>> f, IList<A> list) {
		return (Parser<IList<B>>) Monad.super.mapM(f, list);
	}

	/*
	 * Bridge method to make Monad#forM type aware of {@code Parser<T>}
	 * 
	 * (non-Javadoc)
	 * 
	 * @see edu.parsec.typeclass.Monad#forM(edu.parsec.data.list.IList,
	 * java.util.function.Function)
	 */
	@Override
	public <A, B> Parser<IList<B>> forM(IList<A> list, Function<A, Monad<B, Parser<?>>> f) {
		return (Parser<IList<B>>) Monad.super.forM(list, f);
	}

}
