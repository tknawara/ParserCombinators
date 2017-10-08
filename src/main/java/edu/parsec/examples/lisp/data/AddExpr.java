package edu.parsec.examples.lisp.data;

/**
 * Representation of the add expression {@example 1 + 2}
 *
 * @author Tarek Nawara
 */
public class AddExpr implements Expr {
    private final Expr leftExpr;
    private final Expr rightExpr;

    /**
     * Construct adding expression.
     *
     * @param leftExpr left expression.
     * @param rightExpr right expression.
     */
    public AddExpr(final Expr leftExpr, final Expr rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    @Override
    public double eval() {
        return leftExpr.eval() + rightExpr.eval();
    }

    @Override
    public String toString() {
        return leftExpr + " + " + rightExpr;
    }
}
