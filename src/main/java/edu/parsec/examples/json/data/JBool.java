package edu.parsec.examples.json.data;

/**
 * Representation of Json boolean value.
 *
 * @author Tarek Nawara
 */
public class JBool extends Json {
	public final boolean b;
	
	public JBool(final boolean b) {
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "JBool(" + String.valueOf(b) + ")";
	}
}
