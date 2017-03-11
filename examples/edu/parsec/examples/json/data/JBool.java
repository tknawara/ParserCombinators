package edu.parsec.examples.json.data;

public class JBool extends JSON {
	public final boolean b;
	
	public JBool(boolean b) {
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "JBool(" + String.valueOf(b) + ")";
	}
}
