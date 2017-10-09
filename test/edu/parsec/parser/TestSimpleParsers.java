package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.Combinator;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSimpleParsers {

	@Test public void
	digit_parser_should_parse_digits() {
		final Parser<Character> digitParser = ParserBuilder.digitParser();
		final Result<Pair<Character, IList<Character>>> result = digitParser.run("1");
		assertTrue(result.isSuccess());
		assertEquals(Character.valueOf('1'), result.get().first);
	}

	@Test
	public void
	double_parser_should_parse_any_numbers() {
		final Parser<Double> doubleParser = ParserBuilder.noSpacesParser(ParserBuilder.doubleParser());
		final Result<Pair<Double, IList<Character>>> result = doubleParser.run("12345");
		assertTrue(result.isSuccess());
		assertEquals(Double.valueOf(12345), result.get().first);
	}

	@Test public void
	digit_parser_should_parse_integers() {
		final Parser<Integer> intP = ParserBuilder.intParser();
		final Result<Pair<Integer, IList<Character>>> result = intP.run("123");
		assertTrue(result.isSuccess());
		assertEquals(Integer.valueOf(123), result.get().first);
	}

}
