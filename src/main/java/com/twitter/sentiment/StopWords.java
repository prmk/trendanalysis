package com.twitter.sentiment;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class StopWords implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> stopWords;
	private static StopWords _singleton;

	private StopWords() {
		this.stopWords = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("./data/stop-words.txt"));
			String line = null;
			while ((line = reader.readLine()) != null)
				this.stopWords.add(line);
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

	private static StopWords get() {
		if (_singleton == null)
			_singleton = new StopWords();
		return _singleton;
	}

	public static List<String> getStopWords() {
		return get().stopWords;
	}
}