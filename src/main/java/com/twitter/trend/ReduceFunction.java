package com.twitter.trend;

import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairDStream;

public class ReduceFunction {

	public JavaPairDStream<String, Integer> reduce(
			JavaPairDStream<String, Integer> tuples) {
		JavaPairDStream<String, Integer> counts = tuples.reduceByKeyAndWindow(
				new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer i1, Integer i2) {
						return i1 + i2;
					}
				}, new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer i1, Integer i2) {
						return i1 - i2;
					}
				}, new Duration(60 * 2 * 1000), new Duration(60 * 1000));

		return counts;
	}

}
