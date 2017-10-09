package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class TestAnd {

	@Test public void
	anding_two_parsers_result_sequential_parser() {
		final Parser<Character> a = ParserBuilder.parseChar('a');
		final Parser<Character> b = ParserBuilder.parseChar('b');
		final Parser<Pair<Character, Character>> ab = a.and(() -> b);
		final Result<Pair<Pair<Character, Character>, IList<Character>>> result = ab.run("abc");
		assertTrue(result.isSuccess());
	}

	@Test public void
	anding_two_parsers_and_running_them_should_consume_input_successfully() {
		final Parser<Character> a = ParserBuilder.parseChar('a');
		final Parser<Character> b = ParserBuilder.parseChar('b');
		final Parser<Pair<Character, Character>> ab = a.and(() -> b);
		final Result<Pair<Pair<Character, Character>, IList<Character>>> result = ab.run("abc");
		assertEquals(result.get().first, new Pair<>('a', 'b'));
	}
}
