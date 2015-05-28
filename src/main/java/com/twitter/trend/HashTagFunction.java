package com.twitter.trend;

import org.apache.spark.api.java.function.Function;

public class HashTagFunction implements Function<String, Boolean> {
	private static final long serialVersionUID = 1L;

	public Boolean call(String word) throws Exception {
		return word.startsWith("#");
	}

}
