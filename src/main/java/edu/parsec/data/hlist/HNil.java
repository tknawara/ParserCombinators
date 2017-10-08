package edu.parsec.data.hlist;

public final class HNil implements HList<HNil> {

    private HNil() {}

    public static HNil nil() {
        return new HNil();
    }

    public String toString() {
        return "";
    }
}
