package com.twitter.trend;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;

public class TransFormtoPairFunction implements
		Function<JavaPairRDD<Integer, String>, JavaPairRDD<Integer, String>> {

	private static final long serialVersionUID = 1L;

	public JavaPairRDD<Integer, String> call(JavaPairRDD<Integer, String> in)
			throws Exception {
		return in.sortByKey(false);
	}

}
