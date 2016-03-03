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
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.projectimdbdeadpool.model.FilmData;
import de.projectimdbdeadpool.model.FilmUrl;
import de.projectimdbdeadpool.tools.UtilParse;
import de.projectimdbdeadpool.tools.UtilStoreGcloudFilmData;
import de.projectimdbdeadpool.tools.UtilStoreGcloudFilmUrl;

// [START example]
@SuppressWarnings("serial")
public class CollectFilmDataServlet extends HttpServlet {


@Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    UtilStoreGcloudFilmData utilStore = new UtilStoreGcloudFilmData(this);
    
    List<FilmData> listParsedFilmData = new ArrayList<>();

//    HashMap<String, String> blockbusters = new HashMap<>();
//    blockbusters.put("Deadpool", "http://www.imdb.com/title/tt1431045/");
//    blockbusters.put("Star Wars - Force Awakens", "http://www.imdb.com/title/tt2488496/");
//    blockbusters.put("Batman v Superman", "http://www.imdb.com/title/tt2975590/");
//    blockbusters.put("The Hateful Eight", "http://www.imdb.com/title/tt3460252/");
    
    List<FilmUrl> observedUrls = new UtilStoreGcloudFilmUrl(this).loadFilmUrl();
    
    // parse
    out.print("<table>");
    out.print("<th>Name</th><th>Rating</th><th>Cnt</th>");
    for(FilmUrl url : observedUrls){
    	FilmData data = UtilParse.parseImdbUrl(url.imdbUrl);
    	// remember for storing
    	listParsedFilmData.add(data);
    	
    	if(data == null){
    		out.println("<tr><td colspan='3'>Error occurred on parsing: "+url+"</td></tr>");
    		return;
    	}
    	out.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>",data.name, data.imdbRating, data.imdbRatingCnt ));
    }
    out.print("</table>");
    
    
    // store
    for(FilmData filmData : listParsedFilmData){
    	utilStore.storeFilmData(filmData);
    }
  }

}
// [END example]
