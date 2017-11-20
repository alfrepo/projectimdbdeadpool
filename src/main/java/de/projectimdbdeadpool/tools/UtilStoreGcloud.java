package de.projectimdbdeadpool.tools;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.GenericServlet;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;

public class UtilStoreGcloud {

	public static final String PROJECT_ID = "projectimdbdeadpool";
	public static final String PATH_TO_JSON_KEY = "/WEB-INF/keys/projectimdbdeadpool-a5fcc4480d7c.json";

	protected GenericServlet servlet;

	public UtilStoreGcloud(GenericServlet servlet) {
		this.servlet = servlet;
	}

	public static Datastore initDataStore(GenericServlet servlet) {
		return DatastoreOptions.getDefaultInstance().getService();
	}
	
	protected static void clearAllEntities(GenericServlet servlet, String kind) {
		
		Datastore datastore = initDataStore(servlet);
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(kind).build();
		Iterator<Entity> iterator = datastore.run(query);
		
		// collect keys
		ArrayList<Key> keys = new ArrayList<>();
		while (iterator.hasNext()) {
			keys.add(iterator.next().getKey());
		}
		
		// delete
		datastore.delete(keys.toArray(new Key[keys.size()] ));
	}

}
