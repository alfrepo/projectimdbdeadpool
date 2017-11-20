package de.projectimdbdeadpool.tools;

import javax.servlet.GenericServlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class UtilStoreAppengine {

	public static final String PROJECT_ID = "projectimdbdeadpool";

	protected GenericServlet servlet;

	public UtilStoreAppengine(GenericServlet servlet) {
		this.servlet = servlet;

	}

	public static DatastoreService initDataStore() {
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		return datastore;
	}
	
}
