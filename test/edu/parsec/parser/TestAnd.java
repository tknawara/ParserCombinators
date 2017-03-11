package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;

public class TestAnd {
	public static void main(String[] args) {
		Parser<Character> a = ParserBuilder.parseChar('a');
		Parser<Character> b = ParserBuilder.parseChar('b');
		Parser<Pair<Character, Character>> ab = a.and(() -> b);
		Result<Pair<Pair<Character, Character>, IList<Character>>> result = ab.run("abc");
		System.out.println(result);
	}
}
