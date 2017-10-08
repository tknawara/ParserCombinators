package edu.parsec.examples.lisp.data;

/**
 * Representation of Division expression {@example 1 / 2}
 *
 * @author Tarek Nawara
 */
public class DivExpr implements Expr {
    private final Expr leftExpr;
    private final Expr rightExpr;

    /**
     * Construct a Division Expression.
     *
     * @param leftExpr left subexpression.
     * @param rightExpr right subexpression.
     */
    public DivExpr(final Expr leftExpr, final Expr rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public double eval() {
        return leftExpr.eval() / rightExpr.eval();
    }

    @Override
    public String toString() {
        return leftExpr + " / " + rightExpr;
    }
}
