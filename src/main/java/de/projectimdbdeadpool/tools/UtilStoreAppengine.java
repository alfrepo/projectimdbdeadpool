package de.projectimdbdeadpool.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.GenericServlet;

//import com.google.gcloud.AuthCredentials;
//import com.google.gcloud.datastore.Datastore;
//import com.google.gcloud.datastore.DatastoreOptions;
//import com.google.gcloud.datastore.Entity;
//import com.google.gcloud.datastore.Key;
//import com.google.gcloud.datastore.Query;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreConfig;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;

public class UtilStoreAppengine {

	public static final String PROJECT_ID = "projectimdbdeadpool";
	public static final String PATH_TO_JSON_KEY = "/WEB-INF/keys/projectimdbdeadpool-30d8a0485934.json";

	protected GenericServlet servlet;

	public UtilStoreAppengine(GenericServlet servlet) {
		this.servlet = servlet;
	}

	public static DatastoreService initDataStore() {
//			URL theurl = servlet.getServletContext().getResource(PATH_TO_JSON_KEY);
//			File file = new File(theurl.toURI());
//			FileInputStream fileInputStreamKey = new FileInputStream(file);
//
//			return DatastoreOptions.builder().projectId(PROJECT_ID)
//					.authCredentials(AuthCredentials.createForJson(fileInputStreamKey)).build().service();
			
		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		return datastore;
	}
	
	protected void clearAllEntities(String kind) {
		
//		DatastoreService datastore = initDataStore(servlet);
//		Query<Entity> query = Query.entityQueryBuilder().kind(kind).build();
//		Iterator<Entity> iterator = datastore.run(query);
//		
//		// collect keys
//		ArrayList<Key> keys = new ArrayList<>();
//		while (iterator.hasNext()) {
//			keys.add(iterator.next().key());
//		}
//		
//		// delete
//		datastore.delete(keys.toArray(new Key[keys.size()] ));
	}

}
