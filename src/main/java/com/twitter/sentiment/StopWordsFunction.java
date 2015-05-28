package com.twitter.sentiment;

import java.util.List;

import org.apache.spark.api.java.function.Function;

public class StopWordsFunction implements Function<String, String> {
	private static final long serialVersionUID = 42l;

	public String call(String status) {
		List<String> stopWords = StopWords.getStopWords();
		for (String word : stopWords) {
			status = status.replaceAll("\\b" + word + "\\b", "");
		}
		return status;
	}
}