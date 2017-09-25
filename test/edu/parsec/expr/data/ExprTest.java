package edu.parsec.expr.data;

import edu.parsec.examples.lisp.expr.*;

public class ExprTest {
    public static void main(String[] args) {
        Expr expr = new AddExpr(new DivExpr(new Value(1), new Value(2)), new MulExpr(new Value(3), new Value(4)));
        System.out.println(expr.eval());
    }
}
