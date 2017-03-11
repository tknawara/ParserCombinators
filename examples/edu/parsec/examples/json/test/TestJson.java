package edu.parsec.examples.json.test;

import java.io.File;
import java.util.Scanner;

import edu.parsec.data.list.IList;
import edu.parsec.data.pair.Pair;
import edu.parsec.data.result.Result;
import edu.parsec.examples.json.data.JSON;
import edu.parsec.examples.json.parser.JSONCombinators;
import edu.parsec.parser.imp.Parser;

public class TestJson {
	public static void main(String[] args) {
		StringBuilder inputBuffer = new StringBuilder();
		File f = new File("input.json");
		try (Scanner scan = new Scanner(f)) {
			while (scan.hasNextLine()) {
				inputBuffer.append(scan.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String input = "{ \t\"name\":\"John\",\t \"age\":31\t, \t\"city\":\"NewYork\" }";
		String input = inputBuffer.toString();
		Parser<JSON> parser = JSONCombinators.JsonParser();
		Result<Pair<JSON, IList<Character>>> result = parser.run(input);
		System.out.println(result);

	}
}
