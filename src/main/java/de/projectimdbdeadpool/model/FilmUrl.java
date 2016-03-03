package de.projectimdbdeadpool.model;

import com.google.gcloud.datastore.DateTime;
import com.google.gcloud.datastore.Key;

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
