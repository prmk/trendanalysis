package com.twitter.sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class NegativeWords implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> negativeWordsSet;
	private static NegativeWords _singleton;

	private NegativeWords() {
		this.negativeWordsSet = new HashSet<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("./data/neg-words.txt"));
			while (reader.readLine() != null)
				this.negativeWordsSet.add(reader.readLine());
		} catch (IOException ex) {
			Logger.getLogger(this.getClass()).error(
					"IO error while initializing", ex);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
			}
		}
	}

	private static NegativeWords get() {
		if (_singleton == null)
			_singleton = new NegativeWords();
		return _singleton;
	}

	public static Set<String> getNegativeWords() {
		return get().negativeWordsSet;
	}
}