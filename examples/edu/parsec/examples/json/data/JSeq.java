package edu.parsec.examples.json.data;

import edu.parsec.data.list.IList;

public class JSeq extends JSON {
	public final IList<JSON> elems;

	public JSeq(IList<JSON> elems) {
		this.elems = elems;
	}

	@Override
	public String toString() {
		return "JSeq([" + elems.map(x -> x.toString()).mkString(", ") + "])";
	}
}
