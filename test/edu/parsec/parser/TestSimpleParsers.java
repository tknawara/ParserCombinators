package edu.parsec.parser;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.parser.combinators.Combinators;
import edu.parsec.parser.imp.Parser;

public class TestSimpleParsers {
	public static void main(String[] args) {
		
		Parser<Double> parser = Combinators.spaces().then(Combinators.doubleParser()).skip(Combinators.spaces());
		Result<Pair<Double, IList<Character>>> result = parser.run("    123123      ");
		System.out.println(result);
	}
}
