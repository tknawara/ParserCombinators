package edu.parsec.parser.combinators;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Failure;
import edu.parsec.data.result.Result;
import edu.parsec.data.result.Success;
import edu.parsec.data.unit.Unit;
import edu.parsec.parser.imp.Parser;

/**
 * This class is responsible for any parser construction
 * <p>
 * It is used in all parser usages. no interaction between the parser
 * constructor should happen
 * </p>
 * 
 * @author Tarek Nawara
 *
 */
public class ParserBuilder {

	/**
	 * Given a string, this method will return a parser that when run will
	 * return success if it manages to parse the string from the input
	 * characters or failure otherwise
	 * 
	 * @param expectedString
	 *            the target string to parse
	 * @return parser that will parse the string when it run
	 */
	public static Parser<String> parseString(final String expectedString) {
		IList<Character> list = IList.buildFrom(expectedString);
		return UnitParser().mapM(ParserBuilder::parseChar, list).map(ParserBuilder::charIListToString);
	}

	/**
	 * Get a parser that when run will return unit
	 * 
	 * @return unit
	 * @see {@link Unit}
	 */
	public static Parser<Unit> UnitParser() {
		final Function<IList<Character>, Result<Pair<Unit, IList<Character>>>> innerFunc = new Function<IList<Character>, Result<Pair<Unit, IList<Character>>>>() {
			@Override
			public Result<Pair<Unit, IList<Character>>> apply(final IList<Character> inputChars) {
				final Pair<Unit, IList<Character>> unitPair = new Pair<>(Unit.get(), inputChars);
				return new Success<>(unitPair);
			}
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * Given a character this function will return a parser that when run on a
	 * given input will parse this character
	 * 
	 * @param c
	 *            character to parse from the input characters
	 * @return success if found the character and failure otherwise
	 */
	public static Parser<Character> parseChar(final char c) {
		final Function<IList<Character>, Result<Pair<Character, IList<Character>>>> innerFunc = new Function<IList<Character>, Result<Pair<Character, IList<Character>>>>() {
			@Override
			public Result<Pair<Character, IList<Character>>> apply(final IList<Character> inputChars) {
				if (inputChars.isEmpty()) {
					return new Failure<>(String.format("Expected: '%c', we reached end of string\n", c));
				} else if (inputChars.head() == c) {
					return new Success<>(new Pair<>(c, inputChars.tail()));
				} else {
					return new Failure<>(String.format("Expected: '%c', found: '%c', in %s", c, inputChars.head(),
							inputChars.mkString("")));
				}
			}
		};
		return new Parser<>(innerFunc);
	}

	/**
	 * Return a parser that will parse any of the given characters.
	 * 
	 * @param validChars
	 *            all the possible characters that could be parsed
	 * @return parser that will parse any of the given characters
	 */
	public static Parser<Character> anyCharOf(final List<Character> validChars) {
		final Optional<Parser<Character>> result = validChars.stream().map(ParserBuilder::parseChar)
				.reduce((x, y) -> x.or(() -> y));
		return result.get();
	}

	/**
	 * Same as {@code anyCharOf(List<Character> validChars)} but builds the
	 * parser from string
	 * 
	 * @param s
	 * @return a parser that when run will parser any character of the given
	 *         string
	 */
	public static Parser<Character> anyCharOf(String s) {
		List<Character> validChars = new ArrayList<>();
		for (int i = 0; i < s.length(); ++i) {
			validChars.add(s.charAt(i));
		}
		return ParserBuilder.anyCharOf(validChars);
	}

	private static String charIListToString(final IList<Character> list) {
		return list.foldLeft("", (acc, c) -> acc + c.toString());
	}
}
