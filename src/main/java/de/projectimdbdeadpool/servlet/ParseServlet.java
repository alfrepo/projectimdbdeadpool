package de.projectimdbdeadpool.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gcloud.datastore.Key;

import de.projectimdbdeadpool.model.FilmData;
import de.projectimdbdeadpool.model.FilmUrl;
import de.projectimdbdeadpool.tools.UtilParse;
import de.projectimdbdeadpool.tools.UtilStoreFilmData;
import de.projectimdbdeadpool.tools.UtilStoreFilmUrl;

/**
 * Servlet implementation class ObserverdMoviesServlet
 */
public class ParseServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ParseServlet.class.getName());
	
	private static final long serialVersionUID = 1L;
       
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		handleRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		
		UtilStoreFilmUrl utilStore = new UtilStoreFilmUrl(this);
		
		out.println("<head></head><body>");
		out.println(
				"<form action='#' method='post'>"
				+"imdb Film URL to parse: <input type='text' name='imdburl'>"
				+"<input type='submit' value='Submit'>"
				+"</form>"
				);
		
		// adding new imdb url
		String[] imdburlPar = req.getParameterValues("imdburl");
		if(imdburlPar != null && imdburlPar.length>0){
			FilmData data = new UtilParse().parseImdbUrl(imdburlPar[0]);
			out.println(data.toString());
		}
		
	}

	private void delUrl(PrintWriter out, String imdburlkeyForDeletion) {
		new UtilStoreFilmUrl(this).deleteFilmUrl(imdburlkeyForDeletion);
	}

	private void addNewUrl(PrintWriter out, String imdburlPar) {
		// parse URL
		out.println("Add "+imdburlPar);
		
		// parse
		UtilParse utilParse = new UtilParse();
		
		FilmData filmData = null;
		try {
			filmData = utilParse.parseImdbUrl(imdburlPar);
			out.println("Parsed successfully: "+filmData);
		} catch (IOException e) {
			String msg = "Failed to parse URL "+imdburlPar;
			log.log(Level.WARNING, msg);
			out.println(msg);
			return;
		}
		
		// persist
		FilmUrl url = new FilmUrl();
		url.imdbUrl = filmData.imdbUrl;
		url.name = filmData.name;

		UtilStoreFilmUrl utilStoreFilmUrl = new UtilStoreFilmUrl(this);
		try {
			Key key = utilStoreFilmUrl.storeFilmUrl(url);
			url.entityKey = key;
			out.println("Stored successfully: "+filmData);
			
		} catch (IOException e) {
			String msg = "Failed to store FilmData: "+filmData;
			log.log(Level.WARNING, msg);
			out.println(msg);
			return;
		}
	}
	
	

}
