/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.projectimdbdeadpool.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.projectimdbdeadpool.model.FilmData;
import de.projectimdbdeadpool.tools.UtilStoreGcloudFilmData;

// [START example]
@SuppressWarnings("serial")
public class MainPageServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();

		out.println("<head></head><body>");
		out.println("<ul>");
		out.println("<li><a href='/rest/collect'>Collect current data from IMDB</a></li>");
		out.println("<li><a href='/rest/show'>Show all collected data</a></li>");
		out.println("<li><a href='/rest/show?imdbId=tt2488496'>Show IMDB Id tt2488496</a></li>");
		out.println("<li><a href='/rest/clear'>Clear DB data</a></li>");
		out.println("<li><a href='/rest/observed'>Edit Observed URLs</a></li>");
		out.println("<li><a href='/rest/parse'>Test Parse IMDB URL</a></li>");
		out.println("</ul>");

		
		// retrieve all FilmData
		UtilStoreGcloudFilmData utils = new UtilStoreGcloudFilmData(this);
		List<FilmData> data = utils.loadFilmData();
		
		Map<String, String> urls = new HashMap<>();
		for(FilmData filmData : data){
			urls.put(filmData.name, filmData.imdbUrl);
		}
		
		out.println("<h2>Known films</h2>");
		out.println("<ul>");
		for(String name : urls.keySet()){
			String imdbid = getImdbIdByUrl(urls.get(name));
			out.println(String.format("<li><a href='/static/?imdbid=%s'>%s</a>", imdbid, name));
		}
		out.println("</ul>");
		out.println("</body>");
	}

	private String getImdbIdByUrl(String url) {
		String imdbId = url.replace("http://www.imdb.com/title/", "");
		imdbId = imdbId.replaceAll("/", "");
		return imdbId;
	}
	
	
	
}
// [END example]
