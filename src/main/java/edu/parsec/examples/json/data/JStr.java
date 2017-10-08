package edu.parsec.examples.json.data;

/**
 * Representation of Json string
 *
 * @author Tarek Nawara
 */
public class JStr extends Json {
    public final String str;

    public JStr(final String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "JStr(\"" + str + "\")";
    }
}
