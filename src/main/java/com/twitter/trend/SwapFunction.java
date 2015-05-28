package com.twitter.trend;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SwapFunction implements
		PairFunction<Tuple2<String, Integer>, Integer, String> {

	private static final long serialVersionUID = 1L;

	public Tuple2<Integer, String> call(Tuple2<String, Integer> in)
			throws Exception {
		return in.swap();
	}

}
