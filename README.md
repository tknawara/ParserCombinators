# Parser Combinators

Simple implementation of a monadic parser in Java

## Motivation

- Parsing things in Java is hard compared with functional languages that supports monadic parsers.
This is a simple try to do these stuff in java.

- Introduce a tool to allow parsing recursive structures like JSON in an easy way

### Prerequisites

Basic understanding of how parser combinators works, like how to "And" two parser.

## Examples
### example 1
```java
import edu.parsec.data.list.IList;
import edu.parsec.parser.imp.Parser;
import edu.parsec.parser.combinators.Combinators;
import edu.parsec.parser.combinators.ParserBuilder;

public class IntegerParser {
	public Parser<Integer> integerParser() {
			Parser<Character> digitParser = ParserBuilder.anyCharOf("0123456789");
			Parser<IList<Character>> digits = Combinators.many1(Combinators.digitParser());
		return digits.map(x -> x.foldLeft("", (acc, c) -> acc String.valueOf(c))).map(Integer::parseInt);
	}
}
```
### example 2
```java
import edu.parsec.examples.json.data.JStr;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.combinators.Combinators;

public Parser<JStr> JStrParser() {
		Parser<Character> quote = ParserBuilder.parseChar('\"');
		Parser<String> str = Combinators.stringParser();
		return cleanRun(quote.then(str).skip(quote)).map(JStr::new);
}
```

## Challenges
- Making a better monadic interface. Right now the Monad interface is not type aware of the Monad class extending it. so I used bridge methods to narrow the types to make it suitable for the parser class.
- Make the parse faster by optimizing the building process.

## License

This project is licensed under the MIT License - see the [LICENCE] file for details

