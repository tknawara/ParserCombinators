package edu.parsec.examples.json.test;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.examples.json.data.JSON;
import edu.parsec.examples.json.parser.JSONCombinators;
import edu.parsec.parser.imp.Parser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestJson {
	public static void main(String[] args) throws IOException {
		final String input = FileUtils.readFileToString(new File("input.json"), "UTF-8");
		final Parser<JSON> parser = JSONCombinators.JsonParser();
		final Result<Pair<JSON, IList<Character>>> result = parser.run(input);
		System.out.println(result);
	}
}
