package com.twitter.trendanalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.examples.streaming.StreamingExamples;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import scala.Tuple2;
import scala.Tuple4;
import twitter4j.Status;

import com.twitter.sentiment.SentimentAnalysis;
import com.twitter.trend.HashTagFunction;
import com.twitter.trend.MapFunction;
import com.twitter.trend.ReduceFunction1;
import com.twitter.trend.ReduceFunction2;
import com.twitter.trend.StatusFunction;
import com.twitter.trend.SwapFunction;
import com.twitter.trend.TextFilterFunction;
import com.twitter.trend.TextTrimFunction;
import com.twitter.trend.TransFormtoPairFunction;
import com.twitter.trend.WordsFunction;

public class TrendAnalysis {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err
					.println("Usage: TrendAnalysis <consumer key> <consumer secret> "
							+ "<access token> <access token secret>");
			System.exit(1);
		}

		String currentDir = System.getProperty("user.dir");
		String checkPointDir = currentDir + "/checkpoint/";
		StreamingExamples.setStreamingLogLevels();

		System.setProperty("twitter4j.oauth.consumerKey", args[0]);
		System.setProperty("twitter4j.oauth.consumerSecret", args[1]);
		System.setProperty("twitter4j.oauth.accessToken", args[2]);
		System.setProperty("twitter4j.oauth.accessTokenSecret", args[3]);

		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(
				"TrendAnalysis");
		JavaStreamingContext ssc = new JavaStreamingContext(sparkConf,
				new Duration(12 * 1000));
		JavaDStream<Status> tweets = TwitterUtils.createStream(ssc);

		JavaDStream<String> statuses = tweets.map(new StatusFunction());

		 JavaDStream<String> filtered = statuses
		 .filter(new TextFilterFunction());
		
		 JavaDStream<String> filteredStatuses = filtered
		 .map(new TextTrimFunction());

		JavaDStream<String> words = filteredStatuses.flatMap(new WordsFunction());

		JavaDStream<String> hashTags = words.filter(new HashTagFunction());

		JavaPairDStream<String, Integer> tuples = hashTags
				.mapToPair(new MapFunction());

		JavaPairDStream<String, Integer> counts = tuples.reduceByKeyAndWindow(
				new ReduceFunction1(), new ReduceFunction2(), new Duration(
						12 * 1000), new Duration(12 * 1000));

		JavaPairDStream<Integer, String> swappedCounts = counts
				.mapToPair(new SwapFunction());

		JavaPairDStream<Integer, String> sortedCounts = swappedCounts
				.transformToPair(new TransFormtoPairFunction());

		// new FileWrite().write(statuses, sortedCounts);

		final JavaDStream<String> copyofstatuses = statuses;

		sortedCounts
				.foreach(new Function<JavaPairRDD<Integer, String>, Void>() {
					private static final long serialVersionUID = 1L;
					String[] HTArray;

					public Void call(JavaPairRDD<Integer, String> rdd) {
						String out = "\nTop 10 hashtags:\n";
						String s = "";
						for (Tuple2<Integer, String> t : rdd.take(20)) {
							out = out + t.toString() + "\n";
							s = s + t._2() + "\n";
						}
						HTArray = s.split("\n");
						System.out.println(out);
						copyofstatuses
								.foreach(new Function<JavaRDD<String>, Void>() {
									private static final long serialVersionUID = 1L;
									List<String> STArray;
									String[] STArray2;

									public Void call(JavaRDD<String> status)
											throws Exception {
										STArray = status.toArray();
										STArray2 = (String[]) STArray
												.toString().split("EOS");
										for (String hashtag : HTArray) {
											for (String Status : STArray2) {
												if (Status.contains(hashtag)) {
													int index = Status
															.indexOf("EOT");
													int index2 = Status
															.indexOf("ELL");
													String lat = Status
															.substring(
																	index + 4,
																	index2 - 1);
													String lon = Status
															.substring(
																	index2 + 4,
																	Status.length() - 1);
													String result = hashtag
															+ "\t" + lat + "\t"
															+ lon;
													try (PrintWriter fileout = new PrintWriter(
															new BufferedWriter(
																	new FileWriter(
																			"./data/myfile.xlsx",
																			true)))) {
														fileout.println(result);
													} catch (IOException e) {
														e.printStackTrace();
													}

												}
											}
										}
										return null;
									}
								});
						return null;
					}
				});

		JavaDStream<Tuple4<String, Float, Float, String>> sentimentAnalyzedData = new SentimentAnalysis()
				.analyze(copyofstatuses);

		sentimentAnalyzedData.foreachRDD(new FileSentimentData());

		ssc.checkpoint(checkPointDir);
		ssc.start();
		ssc.awaitTermination();
	}

}
