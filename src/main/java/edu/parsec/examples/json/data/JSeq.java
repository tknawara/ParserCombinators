package edu.parsec.examples.json.data;

import edu.parsec.data.list.IList;

/**
 * Representation of Json array.
 *
 * @author Tarek Nawara
 */
public class JSeq extends Json {
    public final IList<Json> elems;

    public JSeq(final IList<Json> elems) {
        this.elems = elems;
    }

    @Override
    public String toString() {
        return "JSeq([" + elems.map(Object::toString).mkString(", ") + "])";
    }
}
