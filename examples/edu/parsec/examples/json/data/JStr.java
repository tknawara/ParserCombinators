package edu.parsec.examples.json.data;

public class JStr extends JSON {
	public final String str;
	
	public JStr(String str) {
		this.str = str;
	}
	
	@Override
	public String toString() {
		return "JStr(\"" + str + "\")";
	}
}
