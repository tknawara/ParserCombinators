package edu.parsec.parser;

import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;

public class TestOr {
	public static void main(String[] args) {
		Parser<Character> a = ParserBuilder.parseChar('a');
		Parser<Character> b = ParserBuilder.parseChar('b');
		Parser<Character> aOrb = a.or(() -> b);
		System.out.println(aOrb.run("bc"));
	}
}
