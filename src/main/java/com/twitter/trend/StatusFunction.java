package com.twitter.trend;

import org.apache.spark.api.java.function.Function;

import twitter4j.Status;

public class StatusFunction implements Function<Status, String> {
	private static final long serialVersionUID = 1L;

	public String call(Status status) throws Exception {
		String loc1 = "NA";
		String loc2 = "NA";
		if (status.getGeoLocation() != null)
			loc1 = "" + status.getGeoLocation().getLatitude();
		if (status.getGeoLocation() != null)
			loc2 = "" + status.getGeoLocation().getLongitude();
		return status.getText() + " EOT " + loc1 + " ELL " + loc2 + " EOS ";
	}
}
