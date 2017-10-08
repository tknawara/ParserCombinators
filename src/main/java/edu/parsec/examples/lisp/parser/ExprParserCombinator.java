package edu.parsec.examples.lisp.parser;

import edu.parsec.data.unit.Unit;
import edu.parsec.examples.lisp.data.*;
import edu.parsec.parser.combinators.Combinator;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;

public class ExprParserCombinator {

    private static <T> Parser<T> cleanRun(Parser<T> parser) {
        Parser<Unit> spaces = Combinator.spaces();
        return spaces.then(parser).skip(spaces);
    }

    public static Parser<Value> valueParser() {
        return cleanRun(ParserBuilder.doubleParser().map(Value::new));
    }

    public static Parser<AddExpr> addExprParser() {
        final Parser<Character> openBracket = cleanRun(ParserBuilder.parseChar('('));
        final Parser<Character> addCharacter = cleanRun(ParserBuilder.parseChar('+'));
        final Parser<Character> closeBracket = cleanRun(ParserBuilder.parseChar(')'));
        return openBracket.then(addCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new AddExpr(pair.first, pair.second))).skip(closeBracket);
    }

    public static Parser<SubExpr> subExprParser() {
        final Parser<Character> openBracket = cleanRun(ParserBuilder.parseChar('('));
        final Parser<Character> subCharacter = cleanRun(ParserBuilder.parseChar('-'));
        final Parser<Character> closeBracket = cleanRun(ParserBuilder.parseChar(')'));
        return openBracket.then(subCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new SubExpr(pair.first, pair.second))).skip(closeBracket);
    }

    public static Parser<MulExpr> mulExprParser() {
        final Parser<Character> openBracket = cleanRun(ParserBuilder.parseChar('('));
        final Parser<Character> mulCharacter = cleanRun(ParserBuilder.parseChar('*'));
        final Parser<Character> closeBracket = cleanRun(ParserBuilder.parseChar(')'));
        return openBracket.then(mulCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new MulExpr(pair.first, pair.second))).skip(closeBracket);

    }

    public static Parser<DivExpr> divExprParser() {
        final Parser<Character> openBracket = cleanRun(ParserBuilder.parseChar('('));
        final Parser<Character> divCharacter = cleanRun(ParserBuilder.parseChar('/'));
        final Parser<Character> closeBracket = cleanRun(ParserBuilder.parseChar(')'));
        return openBracket.then(divCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new DivExpr(pair.first, pair.second))).skip(closeBracket);
    }

    public static Parser<Expr> exprParser() {
        final Parser<Expr> valueParser = valueParser().map(x -> x);
        return cleanRun(valueParser.or(ExprParserCombinator::addExprParser).or(ExprParserCombinator::subExprParser)
                .or(ExprParserCombinator::mulExprParser).or(ExprParserCombinator::divExprParser));
    }
}
