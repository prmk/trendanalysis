package com.twitter.trend;

import org.apache.spark.api.java.function.Function;

public class TextFilterFunction implements Function<String, Boolean> {

	private static final long serialVersionUID = 1L;

	@Override
	public Boolean call(String status) throws Exception {
		return status != null;
	}

}
