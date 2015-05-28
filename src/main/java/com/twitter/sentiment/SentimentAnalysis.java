package com.twitter.sentiment;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

public class SentimentAnalysis {
	public JavaDStream<Tuple4<String, Float, Float, String>> analyze(
			JavaDStream<String> statuses) {
		statuses = statuses.map(new StopWordsFunction());

		JavaPairDStream<String, Float> positiveStatuses = statuses
				.mapToPair(new PositiveAnalysisFunction());

		JavaPairDStream<String, Float> negativeStatuses = statuses
				.mapToPair(new NegativeAnalysisFunction());

		JavaPairDStream<String, Tuple2<Float, Float>> joinedData = positiveStatuses
				.join(negativeStatuses);

		JavaDStream<Tuple3<String, Float, Float>> combinedData = joinedData
				.map(new CombineFunction());

		JavaDStream<Tuple4<String, Float, Float, String>> analyzedData = combinedData
				.map(new AnalysisFunction());

		return analyzedData;
	}
}