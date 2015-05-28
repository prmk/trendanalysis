package com.twitter.trendanalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;

import scala.Tuple2;

public class FileWrite {

	public void write(final JavaDStream<String> statuses,
			JavaPairDStream<Integer, String> sortedCounts) {
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
						statuses.foreach(new Function<JavaRDD<String>, Void>() {
							private static final long serialVersionUID = 1L;
							List<String> STArray;
							String[] STArray2;

							public Void call(JavaRDD<String> status)
									throws Exception {
								STArray = status.toArray();
								STArray2 = (String[]) STArray.toString().split(
										"EOS");
								for (String hashtag : HTArray) {
									for (String Status : STArray2) {
										if (Status.contains(hashtag)) {
											int index = Status.indexOf("EOT");
											int index2 = Status.indexOf("ELL");
											String lat = Status.substring(
													index + 4, index2 - 1);
											String lon = Status.substring(
													index2 + 4,
													Status.length() - 1);
											String result = hashtag + " " + lat
													+ " " + lon;
											try (PrintWriter fileout = new PrintWriter(
													new BufferedWriter(
															new FileWriter(
																	"myfile.xlsx",
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
	}

}
