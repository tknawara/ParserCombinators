package edu.parsec.examples.json.data;

import java.util.Map;
import java.util.Optional;

/**
 * Representation of a Json object.
 * @author Tarek Nawara
 */
public class JObj extends Json {
    public final Map<String, Json> bindings;

    /**
     * Constructor.
     *
     * @param bindings elements in the json map
     */
    public JObj(final Map<String, Json> bindings) {
        this.bindings = bindings;
    }

    @Override
    public String toString() {
        final Optional<String> s =
                bindings.entrySet().stream()
                        .map(entry -> String.format("\"%s\": %s", entry.getKey(), entry.getValue()))
                        .reduce((a, b) -> a + "," + b);

        return "JObj({" + s.orElse("") + "})";
    }
}
