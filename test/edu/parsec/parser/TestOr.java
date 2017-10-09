package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestOr {

	@Test public void
	oring_two_parsers_should_result_a_parser_that_will_try_both_of_them() {
		final Parser<Character> a = ParserBuilder.parseChar('a');
		final Parser<Character> b = ParserBuilder.parseChar('b');
		final Parser<Character> aOrB = a.or(() -> b);
		final Result<Pair<Character, IList<Character>>> resultOne = aOrB.run("ac");
		final Result<Pair<Character, IList<Character>>> resultTwo = aOrB.run("bc");
		assertTrue(resultOne.isSuccess());
		assertTrue(resultTwo.isSuccess());
	}

	@Test public void
	oring_two_parsers_should_result_a_parser_that_will_call_the_first_parser_of_the_two_first() {
		final Parser<Character> a = ParserBuilder.parseChar('a');
		final Parser<Character> b = ParserBuilder.parseChar('b');
		final Parser<Character> aOrB = a.or(() -> b);
		final Result<Pair<Character, IList<Character>>> result = aOrB.run("ab");
		assertTrue(result.isSuccess());
		assertEquals(Character.valueOf('a'), result.get().first);
		assertEquals(IList.of("b"), result.get().second);
	}

}
