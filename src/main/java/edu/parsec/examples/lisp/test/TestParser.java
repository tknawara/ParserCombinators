package edu.parsec.examples.lisp.test;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.examples.lisp.data.Expr;
import edu.parsec.examples.lisp.parser.ExprParserCombinator;
import edu.parsec.parser.imp.Parser;

public class TestParser {
    public static void main(String[] args) {
        String input = "(+ (- 1 2) (* 4 5))";
        final Parser<Expr> exprParser = ExprParserCombinator.exprParser();
        final Result<Pair<Expr, IList<Character>>> result = exprParser.run(input);
        System.out.println(result.get().first);
        System.out.println(result.get().first.eval());
    }
}
