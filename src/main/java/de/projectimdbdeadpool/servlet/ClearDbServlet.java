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
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.projectimdbdeadpool.tools.UtilStoreFilmData;

// [START example]
@SuppressWarnings("serial")
public class ClearDbServlet extends HttpServlet {

	private static final String MESSAGE_CLEARING_ALL_FILM_DATA = "Clearing all FilmData";
	private static final String MESSAGE_CLEARING_DONE = "Clearing Done!";
	private static final Logger log = Logger.getLogger(ClearDbServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		
		out.println("<html>");
		out.println(
				"<form action='#' method='post'>"
				+"<input type='hidden' name='clearDb' value='true'>"
				+"<input type='submit' value='Clear DB'>"
				+"</form>"
				);
		out.println("</html>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		String[] clearDb = req.getParameterValues("clearDb");
		if(clearDb == null ){
			out.println("Exit without clearing db. Var 'clearDb' was not set!");
			return;
		}
		
		log.warning(MESSAGE_CLEARING_ALL_FILM_DATA);
		out.println(MESSAGE_CLEARING_ALL_FILM_DATA);

		new UtilStoreFilmData(this).clearAllFilmDataFromDatastore();
//		new UtilStoreFilmUrl(this).clearAllFilmUrlFromDatastore();
		
		log.warning(MESSAGE_CLEARING_DONE);
		out.println(MESSAGE_CLEARING_DONE);
		
	}

}
// [END example]
