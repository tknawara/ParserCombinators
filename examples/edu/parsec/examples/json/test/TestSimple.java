package edu.parsec.examples.json.test;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.examples.json.data.JStr;
import edu.parsec.examples.json.parser.JSONCombinators;
import edu.parsec.parser.imp.Parser;

public class TestSimple {
	public static void main(String[] args) {
		Parser<JStr> parser = JSONCombinators.JStrParser();
		Result<Pair<JStr, IList<Character>>> result = parser.run("\"Standard Generalized Markup Language\"");
		System.out.println(result);
	}
}
