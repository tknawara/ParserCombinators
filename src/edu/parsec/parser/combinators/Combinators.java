package edu.parsec.parser.combinators;

import java.util.function.Function;

import edu.parsec.data.list.Empty;
import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Failure;
import edu.parsec.data.result.Result;
import edu.parsec.data.result.Success;
import edu.parsec.data.unit.Unit;
import edu.parsec.parser.imp.Parser;

/**
 * Basic parser combinators. the methods inside of this class is the right way
 * to combine smaller parsers to build more complex ones.
 * 
 * @author Tarek
 *
 */
public class Combinators {

	/**
	 * Run a parser many times over the given input and return a parser that
	 * when run will return a list of its results
	 * 
	 * @param parser
	 *            parser to run
	 * @return a new parser that when run. will consume the input characters as
	 *         much as it can and return the result in a IList
	 */
	public static <A> Parser<IList<A>> many(Parser<A> parser) {
		Function<IList<Character>, Result<Pair<IList<A>, IList<Character>>>> innerFunc = inputChars -> {
			IList<A> result = new Empty<>();
			IList<Character> currentInput = inputChars;
			while (true) {
				Result<Pair<A, IList<Character>>> outer = parser.run(currentInput);
				if (outer.isFailure()) {
					break;
				}
				Pair<A, IList<Character>> pair = outer.get();
				result = result.addFront(pair.first);
				currentInput = pair.second;
			}
			result = result.reverse();
			return new Success<>(new Pair<>(result, currentInput));
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * Run a parser one or more times.
	 * <p>
	 * This method guarantee that the parser will run at least once
	 * </p>
	 * 
	 * @param parser
	 *            parser to run many times
	 * @return a new parser that when run will consume the input as much as it
	 *         can
	 */
	public static <A> Parser<IList<A>> many1(Parser<A> parser) {
		Function<IList<Character>, Result<Pair<IList<A>, IList<Character>>>> innerFunc = inputChars -> {
			IList<A> result = new Empty<>();
			IList<Character> currentInput = inputChars;
			Result<Pair<A, IList<Character>>> outer = parser.run(currentInput);
			if (outer.isFailure()) {
				return new Failure<>(outer.getErrorMessage());
			}
			while (true) {
				Pair<A, IList<Character>> pair = outer.get();
				result = result.addFront(pair.first);
				currentInput = pair.second;
				outer = parser.run(currentInput);
				if (outer.isFailure()) {
					break;
				}
			}
			result = result.reverse();
			return new Success<>(new Pair<>(result, currentInput));
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * Same as {@code many} but this method throws away the result of running
	 * the parser
	 * <p>
	 * This method very suitable in situations where you want to clear
	 * Whitespace from the input stream
	 * </p>
	 * 
	 * @param parser
	 *            parser to run.
	 * @return a parser that will run many times on the input stream but throw
	 *         the result away
	 */
	public static <A> Parser<Unit> skipMany(Parser<A> parser) {
		Function<IList<Character>, Result<Pair<Unit, IList<Character>>>> innerFunc = inputChars -> {
			IList<Character> currentInput = inputChars;
			while (true) {
				Result<Pair<A, IList<Character>>> outer = parser.run(currentInput);
				if (outer.isFailure()) {
					break;
				}
				Pair<A, IList<Character>> pair = outer.get();
				currentInput = pair.second;
			}
			return new Success<>(new Pair<>(Unit.get(), currentInput));
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * Basic parser to parse a single digit
	 * 
	 * @return a parser that when run will parse a single digit
	 */
	public static Parser<Character> digitParser() {
		return ParserBuilder.anyCharOf("0123456789");
	}

	/**
	 * Basic parser that when run will parse a single integer
	 * 
	 * @return parser that when run will try to consume an integer from the
	 *         input stream
	 */
	public static <A> Parser<Integer> intParser() {
		Parser<IList<Character>> digits = Combinators.many1(Combinators.digitParser());
		return digits.map(x -> x.foldLeft("", (acc, c) -> acc + String.valueOf(c))).map(Integer::parseInt);
	}

	/**
	 * Basic parser that when run will return a double
	 * 
	 * @return parser that when run will try to consume a double from an input
	 *         stream
	 */
	public static <A> Parser<Double> doubleParser() {
		Parser<Character> dot = ParserBuilder.parseChar('.');
		Parser<IList<Character>> digits = Combinators.many1(Combinators.digitParser());
		Parser<IList<Character>> chars = (digits.and(() -> dot).map(pair -> pair.first.addFront(pair.second))
				.and(() -> digits).map(pair -> pair.first.append(pair.second))).or(() -> digits);
		return chars.map(l -> l.foldLeft("", (acc, c) -> acc + String.valueOf(c))).map(Double::parseDouble);
	}

	/**
	 * Basic parser that will skip any spaces in the input stream
	 * 
	 * @return parser that will clean the input stream
	 */
	public static Parser<Unit> spaces() {
		Parser<Character> spaceParser = ParserBuilder.anyCharOf("\n\t \r");
		return Combinators.skipMany(spaceParser);
	}

	/**
	 * Basic parser that will parse any input from the input stream
	 * 
	 * @return parser that will parse any thing from the input stream
	 */
	public static Parser<String> stringParser() {
		String alpha = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()#@-_+-*&^%$!~?<>,";
		Parser<Character> charParser = ParserBuilder.anyCharOf(alpha);
		return Combinators.many(charParser).map(l -> l.foldLeft("", (acc, c) -> acc + String.valueOf(c)));
	}
}
