package com.twitter.sentiment;

import org.apache.spark.api.java.function.*;

import scala.Tuple2;

import java.util.Set;

public class PositiveAnalysisFunction implements
		PairFunction<String, String, Float> {

	private static final long serialVersionUID = 1L;

	public Tuple2<String, Float> call(String status) {
		Set<String> positiveWords = PositiveWords.getPositiveWords();
		String[] words = status.split(" ");
		int positiveWordsCount = 0;
		for (String word : words) {
			if (positiveWords.contains(word))
				positiveWordsCount++;
		}

		return new Tuple2<String, Float>(status, (float) positiveWordsCount
				/ words.length);
	}
}