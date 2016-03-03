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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.projectimdbdeadpool.model.FilmData;
import de.projectimdbdeadpool.tools.UtilStoreFilmData;

// [START example]
@SuppressWarnings("serial")
public class AllFilmDataServlet extends HttpServlet {

	private static final String GET_PARAMETER_NAME_IMDB_ID = "imdbId"; // tt2488496 or tt1431045
	private static final Logger log = Logger.getLogger(AllFilmDataServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.warning("AllFilmDataServlet.get()");
		
		PrintWriter out = resp.getWriter();
		UtilStoreFilmData utilStore = new UtilStoreFilmData(this);

		// allow to request this data from other servers
		addCorsHeader(resp);
		
		// get the "imdbId" parameters
		String getParamsImdbId = getGetParameter(req, GET_PARAMETER_NAME_IMDB_ID);
		
		// request film data
		List<FilmData> filmData = utilStore.loadFilmData(getParamsImdbId);

		// sort filmData by imdbUrl into the map
		Map<String, ArrayList<FilmData>> filmsMap = new HashMap<>();
		for (FilmData d : filmData) {
			ArrayList<FilmData> thisFilmData = filmsMap.get(d.imdbUrl);
			if(thisFilmData == null){
				thisFilmData = new ArrayList<>();
				filmsMap.put(d.imdbUrl, thisFilmData);
			}
			thisFilmData.add(d);
		}
		
		// Array contains all data for all films
		JsonArrayBuilder arrayBuilderAllFilmdata = Json.createArrayBuilder();
		
		// create a JSON object for each film 
		for(String imdbUrl:filmsMap.keySet()){
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("imdburl", imdbUrl);
			
			JsonArrayBuilder arrayBuilderFilmdata = Json.createArrayBuilder();
			
			for(FilmData data :filmsMap.get(imdbUrl)){
				arrayBuilderFilmdata.add(
						Json.createObjectBuilder()
							.add("created", data.created.toString())
							.add("name", data.name)
							.add("imdbRating", data.imdbRating)
							.add("imdbRatingCnt", data.imdbRatingCnt)
				);
			}
			
			objectBuilder.add("filmdata", arrayBuilderFilmdata);		
			arrayBuilderAllFilmdata.add(objectBuilder);
		}
		
		// now build the JSON Array
		JsonArray jsonArray = arrayBuilderAllFilmdata.build();
		
		String result = "{}";
		if(jsonArray.size() == 1){
			result = jsonArray.get(0).toString();
		}else if(jsonArray.size() > 1){
			result = jsonArray.toString();
		}
		
		// print
		out.println(result);

	}
	
	private void addCorsHeader(HttpServletResponse response){
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

	private String getGetParameter(HttpServletRequest req, String paramaterName) {
		String[] arrayGetParamsImdbId = req.getParameterValues(paramaterName);
		String getParamsImdbId = null;
		if(arrayGetParamsImdbId!=null && arrayGetParamsImdbId.length>0){
			getParamsImdbId = arrayGetParamsImdbId[0];
		}
		return getParamsImdbId;
	}

}
// [END example]
