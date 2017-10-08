package edu.parsec.examples.lisp.parser;

import edu.parsec.examples.lisp.data.*;
import edu.parsec.parser.combinators.ParserBuilder;
import edu.parsec.parser.imp.Parser;

/**
 * Parser supplier for the expression.
 *
 * @author Tarek Nawara
 */
public class ExprParserCombinator {

    /**
     * Build a parser that parses an arithmetic expression.
     *
     * @return parser that parses lisp expression
     */
    public static Parser<Expr> exprParser() {
        final Parser<Expr> valueParser = valueParser().map(x -> x);
        return ParserBuilder.noSpacesParser(valueParser
                .or(ExprParserCombinator::addExprParser).or(ExprParserCombinator::subExprParser)
                .or(ExprParserCombinator::mulExprParser).or(ExprParserCombinator::divExprParser));
    }

    private static Parser<Value> valueParser() {
        return ParserBuilder.noSpacesParser(ParserBuilder.doubleParser().map(Value::new));
    }

    private static Parser<AddExpr> addExprParser() {
        final Parser<Character> openBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('('));
        final Parser<Character> addCharacter = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('+'));
        final Parser<Character> closeBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar(')'));
        return openBracket.then(addCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new AddExpr(pair.first, pair.second))).skip(closeBracket);
    }

    private static Parser<SubExpr> subExprParser() {
        final Parser<Character> openBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('('));
        final Parser<Character> subCharacter = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('-'));
        final Parser<Character> closeBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar(')'));
        return openBracket.then(subCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new SubExpr(pair.first, pair.second))).skip(closeBracket);
    }

    private static Parser<MulExpr> mulExprParser() {
        final Parser<Character> openBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('('));
        final Parser<Character> mulCharacter = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('*'));
        final Parser<Character> closeBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar(')'));
        return openBracket.then(mulCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new MulExpr(pair.first, pair.second))).skip(closeBracket);

    }

    private static Parser<DivExpr> divExprParser() {
        final Parser<Character> openBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('('));
        final Parser<Character> divCharacter = ParserBuilder.noSpacesParser(ParserBuilder.parseChar('/'));
        final Parser<Character> closeBracket = ParserBuilder.noSpacesParser(ParserBuilder.parseChar(')'));
        return openBracket.then(divCharacter).then(exprParser().and(ExprParserCombinator::exprParser)
                .map(pair -> new DivExpr(pair.first, pair.second))).skip(closeBracket);
    }

}
