package de.projectimdbdeadpool.model;

import org.joda.time.DateTime;

import com.google.cloud.datastore.Key;

public class FilmUrl {
	public Key entityKey = null;
	public String name = "";
	public String imdbUrl = "";
	public DateTime created = DateTime.now();
	
	public boolean isValid() {
		return imdbUrl!=null
				&& name != null
				&& !imdbUrl.isEmpty();
	}
}
