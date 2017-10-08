package edu.parsec.data.hlist;

public class HCons<E, L extends HList<L>> implements HList<HCons<E,L>> {

    public final E head;
    public final L tail;

    public HCons(final E head, final L tail) {
        this.head = head;
        this.tail = tail;
    }

    public String toString() {
        return head.toString() + tail.toString();
    }
}
