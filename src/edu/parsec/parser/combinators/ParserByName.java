package edu.parsec.parser.combinators;

import edu.parsec.parser.imp.Parser;

/**
 * Simple class to simulate a pass by name from <tt>Scala</tt> but in java
 *
 * @param <T> the type of the parser to pass by name
 * @author Tarek
 */
@FunctionalInterface
public interface ParserByName<T> {

    /**
     * Get the parser from the function
     *
     * @return a parser of type {@code T}
     */
     Parser<T> get();
}
