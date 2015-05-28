package com.twitter.sentiment;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;
import scala.Tuple3;

public class CombineFunction
		implements
		Function<Tuple2<String, Tuple2<Float, Float>>, Tuple3<String, Float, Float>> {

	private static final long serialVersionUID = 1L;

	@Override
	public Tuple3<String, Float, Float> call(
			Tuple2<String, Tuple2<Float, Float>> status) throws Exception {
		String stat = status._1();
		Float val1 = status._2._1();
		Float val2 = status._2._2();
		return new Tuple3<String, Float, Float>(stat, val1, val2);
	}

}
