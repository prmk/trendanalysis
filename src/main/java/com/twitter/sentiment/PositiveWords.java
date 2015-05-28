package com.twitter.sentiment;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public class PositiveWords implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> posWords;
	private static PositiveWords _singleton;

	private PositiveWords() {
		this.posWords = new HashSet<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("./data/pos-words.txt"));
			while (reader.readLine() != null)
				this.posWords.add(reader.readLine());
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

	private static PositiveWords get() {
		if (_singleton == null)
			_singleton = new PositiveWords();
		return _singleton;
	}

	public static Set<String> getPositiveWords() {
		return get().posWords;
	}
}