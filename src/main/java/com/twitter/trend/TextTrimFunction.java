package com.twitter.trend;

import org.apache.spark.api.java.function.*;

public class TextTrimFunction implements Function<String, String> {
	private static final long serialVersionUID = 1L;

	public String call(String status) {
		return status.replaceAll("[\n]+", " ").trim().toLowerCase();
	}
}