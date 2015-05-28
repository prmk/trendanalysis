package com.twitter.trend;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class MapFunction implements PairFunction<String, String, Integer> {
	private static final long serialVersionUID = 1L;

	public Tuple2<String, Integer> call(String in) throws Exception {
		return new Tuple2<String, Integer>(in, 1);
	}
}
