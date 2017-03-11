package edu.parsec.examples.json.data;

import java.util.Map;

public class JObj extends JSON {
	public final Map<String, JSON> bindings;

	public JObj(Map<String, JSON> bindings) {
		this.bindings = bindings;
	}

	@Override
	public String toString() {
		StringBuilder assocs = new StringBuilder();
		for (Map.Entry<String, JSON> entry : bindings.entrySet()) {
			assocs.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue().toString()).append(",");
		}
		String s = "";
		if (assocs.length() > 0) {
			s = assocs.subSequence(0, assocs.length() - 1).toString();
		}
		return "JObj({" + s + "})";
	}
}
