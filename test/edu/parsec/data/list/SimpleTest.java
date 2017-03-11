package edu.parsec.data.list;

import java.util.Arrays;
import java.util.List;

import edu.parsec.data.list.IList;

public class SimpleTest {
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		IList<Integer> l = IList.buildFrom(list.iterator());
		System.out.println(l);
		
		System.out.println(l.reverse());
	}
}
