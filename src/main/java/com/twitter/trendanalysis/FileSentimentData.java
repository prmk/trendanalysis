package com.twitter.trendanalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;

import scala.Tuple4;

public class FileSentimentData implements
		Function2<JavaRDD<Tuple4<String, Float, Float, String>>, Time, Void> {
	private static final long serialVersionUID = 1L;

	String content = null;

	public Void call(JavaRDD<Tuple4<String, Float, Float, String>> data,
			Time time) {
		data.foreach(new VoidFunction<Tuple4<String, Float, Float, String>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple4<String, Float, Float, String> result)
					throws Exception {
				try (PrintWriter fileout = new PrintWriter(new BufferedWriter(
						new FileWriter("./data/SentimentResult.xlsx", true)))) {
					String status = result._1();
					status = status.substring(0, status.indexOf("EOT"));
					String fileContents = status + "\t" + result._2() + "\t"
							+ result._3() + "\t" + result._4();
					fileout.println(fileContents);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return null;
	}
}