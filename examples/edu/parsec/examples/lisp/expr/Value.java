package edu.parsec.examples.lisp.expr;

/**
 * Representation of a single value {@example 1.0, 2.0 }
 *
 * @author Tarek Nawara
 */
public class Value implements Expr {
    private final double value;

    /**
     * Construct a value class.
     *
     * @param value actual value
     */
    public Value(double value) {
        this.value = value;
    }

    @Override
    public double eval() {
        return value;
    }
}
