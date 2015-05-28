package com.twitter.sentiment;

import org.apache.spark.api.java.function.*;

import scala.Tuple3;
import scala.Tuple4;

public class AnalysisFunction
		implements
		Function<Tuple3<String, Float, Float>, Tuple4<String, Float, Float, String>> {

	private static final long serialVersionUID = 1L;

	public Tuple4<String, Float, Float, String> call(
			Tuple3<String, Float, Float> tweet) {
		String score;
		if (tweet._2() >= tweet._3())
			score = "positive";
		else
			score = "negative";
		return new Tuple4<String, Float, Float, String>(tweet._1(), tweet._2(),
				tweet._3(), score);
	}
}