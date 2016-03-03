package de.projectimdbdeadpool.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import de.projectimdbdeadpool.tools.UtilStoreAppengine;

/**
 * Servlet implementation class ObserverdMoviesServlet
 */
public class AppengineApiStorageServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(AppengineApiStorageServlet.class.getName());
	
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
		
		UtilStoreAppengine utilStoreAppengine = new UtilStoreAppengine(this);
		DatastoreService datastore = utilStoreAppengine.initDataStore();
		
		// STORE
		Entity employee = new Entity("Employee");

		employee.setProperty("firstName", "Antonio");
		employee.setProperty("lastName", "Salieri");

		Date hireDate = new Date();
		employee.setProperty("hireDate", hireDate);

		employee.setProperty("attendedHrTraining", true);

		datastore.put(employee);
		
		
		
		// Use class Query to assemble a query
		Query q = new Query("Employee");

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		out.println("Employee:");
		for (Entity result : pq.asIterable()) {
		  out.println("result:"+result);
		}
	}

	
	

}
