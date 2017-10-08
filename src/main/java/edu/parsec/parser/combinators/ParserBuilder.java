package edu.parsec.parser.combinators;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Failure;
import edu.parsec.data.result.Result;
import edu.parsec.data.result.Success;
import edu.parsec.data.unit.Unit;
import edu.parsec.parser.imp.Parser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is responsible for any parser construction
 * <p>
 * It is used in all parser usages. no interaction between the parser
 * constructor should happen
 * </p>
 *
 * @author Tarek Nawara
 */
public class ParserBuilder {

    private ParserBuilder() {
    }

    /**
     * Given a string, this method will return a parser that when run will
     * return success if it manages to parse the string from the input
     * characters or failure otherwise
     *
     * @param expectedString the target string to parse
     * @return parser that will parse the string when it run
     */
    public static Parser<String> parseString(final String expectedString) {
        IList<Character> list = IList.of(expectedString);
        return UnitParser().mapM(ParserBuilder::parseChar, list).map(ParserBuilder::charIListToString);
    }

    /**
     * Get a parser that when run will return unit
     *
     * @return unit
     * @see Unit
     */
    public static Parser<Unit> UnitParser() {
        final Function<IList<Character>, Result<Pair<Unit, IList<Character>>>> innerFunc = inputChars -> {
            final Pair<Unit, IList<Character>> unitPair = new Pair<>(Unit.get(), inputChars);
            return new Success<>(unitPair);
        };
        return new Parser<>(innerFunc);
    }

    /**
     * Given a character this function will return a parser that when run on a
     * given input will parse this character
     *
     * @param c character to parse from the input characters
     * @return success if found the character and failure otherwise
     */
    public static Parser<Character> parseChar(final char c) {
        final Function<IList<Character>, Result<Pair<Character, IList<Character>>>> innerFunc = inputChars -> {
            if (inputChars.isEmpty()) {
                return new Failure<>(String.format("Expected: '%c', we reached end of string\n", c));
            } else if (inputChars.head() != c) {
                return new Failure<>(String.format("Expected: '%c', found: '%c', in %s", c, inputChars.head(),
                        inputChars.mkString("")));
            }
            return new Success<>(new Pair<>(c, inputChars.tail()));
        };
        return new Parser<>(innerFunc);
    }

    /**
     * Return a parser that will parse any of the given characters.
     *
     * @param validChars all the possible characters that could be parsed
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
     * @param choices all the possible accepted values
     * @return a parser that when run will parser any character of the given
     * string
     */
    public static Parser<Character> anyCharOf(final String choices) {
        List<Character> validChars = choices.chars().mapToObj(e -> (char) e)
                .collect(Collectors.toList());
        return ParserBuilder.anyCharOf(validChars);
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
     * input stream
     */
    public static <A> Parser<Integer> intParser() {
        Parser<IList<Character>> digits = Combinator.many1(digitParser());
        return digits.map(x -> x.foldLeft("", (acc, c) -> acc + String.valueOf(c)))
                .map(Integer::parseInt);
    }

    /**
     * Create a parser that don't care about spaces in the input.
     *
     * @param parser target parser
     * @param <T> type of element returned by the target parser
     * @return a new parser that skips spaces in the input
     */
    public static <T> Parser<T> noSpacesParser(final Parser<T> parser) {
        Parser<Unit> spaces = Combinator.spaces();
        return spaces.then(parser).skip(spaces);
    }

    /**
     * Basic parser that when run will return a double
     *
     * @return parser that when run will try to consume a double from an input
     * stream
     */
    public static <A> Parser<Double> doubleParser() {
        Parser<Character> dot = ParserBuilder.parseChar('.');
        Parser<IList<Character>> digits = Combinator.many1(digitParser());
        Parser<IList<Character>> chars = (digits.and(() -> dot).map(pair -> pair.first.addFront(pair.second))
                .and(() -> digits).map(pair -> pair.first.append(pair.second))).or(() -> digits);
        return chars.map(l -> l.foldLeft("", (acc, c) -> acc + String.valueOf(c)))
                .map(Double::parseDouble);
    }

    private static String charIListToString(final IList<Character> list) {
        return list.foldLeft("", (acc, c) -> acc + c.toString());
    }

}
