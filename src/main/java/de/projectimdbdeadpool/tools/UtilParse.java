package de.projectimdbdeadpool.tools;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.projectimdbdeadpool.model.FilmData;


public class UtilParse {

	private static final String PARSE_ERROR_TEXT = "";

	/**
	 * Parses the imdb url for film data
	 */
	public static FilmData parseImdbUrl(String url) throws IOException {
		Elements elements;
		Element titleBarWrapper;

		Validate.notNull(url);

		FilmData result = new FilmData();
		result.imdbUrl = url;

		Document doc = Jsoup.connect(url)
				.data("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.data("Accept-Encoding", "gzip, deflate")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0")
				.cookie("auth", "token").timeout(3000).get();

		try {
			elements = doc.select("#title-overview-widget .title_bar_wrapper");
			Validate.isTrue(elements.size() == 1);
			titleBarWrapper = elements.get(0);
		} catch (Exception e) {
			return null;
		}

		try {
			elements = titleBarWrapper.select(".imdbRating strong span");
			String ranking = elements.get(0).text();
			result.imdbRating = ranking;
		} catch (Exception e) {
			result.imdbRating = PARSE_ERROR_TEXT;
		}

		try {
			elements = titleBarWrapper.select(".imdbRating a span.small");
			String cnt = elements.get(0).text();
			result.imdbRatingCnt = cnt;
		} catch (Exception e) {
			result.imdbRatingCnt = PARSE_ERROR_TEXT;
		}

		try {
			elements = titleBarWrapper.select(".title_wrapper h1");
			Element nameFull = elements.get(0);
			nameFull.select("#titleYear").remove();
			String filmName = nameFull.text();
			result.name = filmName;
		} catch (Exception e) {
			result.name = PARSE_ERROR_TEXT;
		}

		return result;
	}
}
