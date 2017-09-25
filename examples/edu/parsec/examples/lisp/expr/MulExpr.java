package edu.parsec.examples.lisp.expr;

/**
 * Representation of multiplication expression
 * {@example 1 * 2}
 *
 * @author Tarek Nawara
 */
public class MulExpr implements Expr {
    private final Expr leftExpr;
    private final Expr rightExpr;

    /**
     * Construct a Multiplication expression.
     *
     * @param leftExpr left expression.
     * @param rightExpr right expression.
     */
    public MulExpr(final Expr leftExpr, final Expr rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public double eval() {
        return this.leftExpr.eval() * this.rightExpr.eval();
    }

    @Override
    public String toString() {
        return leftExpr + " * " + rightExpr;
    }
}
