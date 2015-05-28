package com.twitter.trend;

import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;

public class WordsFunction implements FlatMapFunction<String, String> {
	private static final long serialVersionUID = 1L;

	public Iterable<String> call(String status) throws Exception {
		return Arrays.asList(status.split(" "));
	}

}
