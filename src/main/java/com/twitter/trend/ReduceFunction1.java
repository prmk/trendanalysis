package com.twitter.trend;

import org.apache.spark.api.java.function.Function2;

public class ReduceFunction1 implements Function2<Integer, Integer, Integer> {

	private static final long serialVersionUID = 1L;

	public Integer call(Integer v1, Integer v2) throws Exception {
		return v1 + v2;
	}

}
