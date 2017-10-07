package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.Combinator;
import edu.parsec.parser.imp.Parser;

public class TestSimpleParsers {
	public static void main(String[] args) {
		
		Parser<Double> parser = Combinator.spaces().then(Combinator.doubleParser()).skip(Combinator.spaces());
		Result<Pair<Double, IList<Character>>> result = parser.run("    123123      ");
		System.out.println(result);
	}
}
