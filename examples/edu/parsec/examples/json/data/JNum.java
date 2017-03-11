package edu.parsec.examples.json.data;

public class JNum extends JSON {
	public final double num;
	
	public JNum(double num) {
		this.num = num;
	}
	
	@Override
	public String toString() {
		return "JNum(" + String.valueOf(num) + ")";
	}
}
