package com.twitter.sentiment;

import org.apache.spark.api.java.function.*;
import scala.Tuple2;

import java.util.Set;

public class NegativeAnalysisFunction implements PairFunction<String,String,Float> {
	private static final long serialVersionUID = 1L;

	public Tuple2<String, Float> call(String status) {
		Set<String> negativeWords = NegativeWords.getNegativeWords();
		String[] words = status.split(" ");
		int negativeWordsCount = 0;
		for (String word : words) {
			if (negativeWords.contains(word))
				negativeWordsCount++;
		}
		return new Tuple2<String, Float>(status, (float) negativeWordsCount
				/ words.length);
	}
}