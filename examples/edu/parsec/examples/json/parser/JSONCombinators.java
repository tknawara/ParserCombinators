package edu.parsec.examples.json.parser;

import java.util.HashMap;
import java.util.Map;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.unit.Unit;
import edu.parsec.examples.json.data.JBool;
import edu.parsec.examples.json.data.JNull;
import edu.parsec.examples.json.data.JNum;
import edu.parsec.examples.json.data.JObj;
import edu.parsec.examples.json.data.JSON;
import edu.parsec.examples.json.data.JSeq;
import edu.parsec.examples.json.data.JStr;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.combinators.Combinators;
import edu.parsec.parser.imp.Parser;

public class JSONCombinators {

	private static <T> Parser<T> cleanRun(Parser<T> parser) {
		Parser<Unit> spaces = Combinators.spaces();
		return spaces.then(parser).skip(spaces);
	}

	public static Parser<JNull> JNullParser() {
		return cleanRun(ParserBuilder.parseString("null")).map(x -> new JNull());
	}

	public static Parser<JBool> JBoolParser() {
		Parser<String> trueParser = ParserBuilder.parseString("true");
		Parser<String> falseParser = ParserBuilder.parseString("false");
		Parser<String> trueOrFalse = trueParser.or(() -> falseParser);
		return cleanRun(trueOrFalse).map(Boolean::parseBoolean).map(JBool::new);
	}

	public static Parser<JNum> JNumParser() {
		return cleanRun(Combinators.doubleParser()).map(JNum::new);
	}

	public static Parser<JStr> JStrParser() {
		Parser<Character> quote = ParserBuilder.parseChar('\"');
		Parser<String> str = Combinators.stringParser();
		return cleanRun(quote.then(str).skip(quote)).map(JStr::new);
	}

	public static Parser<JObj> JObjParser() {
		Parser<Character> colon = cleanRun(ParserBuilder.parseChar(':'));
		Parser<Character> comma = cleanRun(ParserBuilder.parseChar(','));
		Parser<IList<Character>> commas = Combinators.many(comma);
		Parser<Character> quote = cleanRun(ParserBuilder.parseChar('\"'));
		Parser<Character> openBrace = cleanRun(ParserBuilder.parseChar('{'));
		Parser<Character> closeBrace = cleanRun(ParserBuilder.parseChar('}'));
		Parser<String> str = cleanRun(quote.then(Combinators.stringParser()).skip(quote));
		Parser<Pair<String, JSON>> binding = str.skip(colon).and(() -> JsonParser()).skip(commas);
		Parser<JObj> body = Combinators.many(binding).map(l -> {
			Map<String, JSON> bindings = toMap(l);
			return new JObj(bindings);
		});
		Parser<JObj> result = openBrace.then(body).skip(closeBrace);
		return result;
	}

	public static Parser<JSeq> JSeqParser() {
		Parser<Character> openBracket = cleanRun(ParserBuilder.parseChar('['));
		Parser<Character> closeBracket = cleanRun(ParserBuilder.parseChar(']'));
		Parser<Character> comma = cleanRun(ParserBuilder.parseChar(','));
		Parser<IList<Character>> commas = Combinators.many(comma);
		Parser<IList<JSON>> token = Combinators.many(JsonParser().skip(commas));
		return openBracket.then(token).skip(closeBracket).map(JSeq::new);
	}

	public static Parser<JSON> JsonParser() {
		Parser<JSON> jNullParser = JNullParser().map(x -> x);
		return jNullParser.or(() -> JBoolParser()).or(() -> JNumParser()).or(() -> JStrParser()).or(() -> JSeqParser()).or(() -> JObjParser());
	}

	private static Map<String, JSON> toMap(IList<Pair<String, JSON>> l) {
		Map<String, JSON> bindings = new HashMap<>();
		l.foldLeft(bindings, (acc, pair) -> {
			acc.put(pair.first, pair.second);
			return acc;
		});
		return bindings;
	}
}
