package edu.parsec.data.unit;

/**
 * Representation of the unit value in all functional programming languages
 *
 * @author Tarek
 */
public final class Unit {

    private static final Unit u = new Unit();

    private Unit() {
    }

    /**
     * The only value of the unit type.
     *
     * @return The only value of the unit type.
     */
    public static Unit get() {
        return u;
    }

    @Override
    public String toString() {
        return "unit";
    }
}