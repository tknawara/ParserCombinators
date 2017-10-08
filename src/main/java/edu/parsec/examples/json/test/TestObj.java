package edu.parsec.examples.json.test;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.unit.Unit;
import edu.parsec.examples.json.data.Json;
import edu.parsec.examples.json.parser.JSONCombinators;
import edu.parsec.parser.combinators.Combinator;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;

public class TestObj {
	public static void main(String[] args) {
		Parser<Character> colon = cleanRun(ParserBuilder.parseChar(':'));
		Parser<Character> comma = cleanRun(ParserBuilder.parseChar(','));
		Parser<IList<Character>> commas = Combinator.many(comma);
		Parser<Character> quote = cleanRun(ParserBuilder.parseChar('\"'));
		Parser<String> str = cleanRun(quote.then(Combinator.stringParser()).skip(quote));
		Parser<Pair<String, Json>> binding = str.skip(colon).and(() -> JSONCombinators.JsonParser()).skip(commas);
		Parser<IList<Pair<String, Json>>> bindings = Combinator.many1(binding);
		
		String input = "\"id\": \"0001\",	\"type\": \"donut\",	\"name\": \"Cake\",	\"ppu\": 0.55";
		System.out.println(bindings.run(input));
	}
	
	public static <T> Parser<T> cleanRun(Parser<T> parser) {
		Parser<Unit> spaces = Combinator.spaces();
		return spaces.then(parser).skip(spaces);
	}
}
