package de.projectimdbdeadpool.model;




import org.joda.time.DateTime;

import com.google.cloud.datastore.Key;

import de.projectimdbdeadpool.tools.UtilsConvert;

public final class FilmData{
	
	public Key entityKey = null;
	public String name = "";
	public String imdbRating = "";
	public String imdbRatingCnt = "";
	public String imdbUrl = "";
	public DateTime created  = DateTime.now();
	
	// amount of rankings?
	// names of the reviewers?
	
	public boolean isValid(){
		return 
				!name.isEmpty() &&
				!imdbRating.isEmpty() &&
				!imdbRatingCnt.isEmpty() &&
				!imdbUrl.isEmpty() &&
				UtilsConvert.toDouble(imdbRating) != null &&
				UtilsConvert.toLong(imdbRatingCnt) != null ;
	}
	
	@Override
	public String toString() {
		return String.format("Date: %s, Name: %s, Rating: %s, RatingCnt: %s, URL: %s", 
				created, name, imdbRating, imdbRatingCnt, imdbUrl );
	}
}