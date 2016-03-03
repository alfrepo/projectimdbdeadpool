package de.projectimdbdeadpool.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.GenericServlet;

import com.google.gcloud.AuthCredentials;
import com.google.gcloud.datastore.Datastore;
import com.google.gcloud.datastore.DatastoreOptions;
import com.google.gcloud.datastore.Entity;
import com.google.gcloud.datastore.Key;
import com.google.gcloud.datastore.Query;

public class UtilStore {

	public static final String PROJECT_ID = "projectimdbdeadpool";
	public static final String PATH_TO_JSON_KEY = "/WEB-INF/keys/projectimdbdeadpool-30d8a0485934.json";

	protected GenericServlet servlet;

	public UtilStore(GenericServlet servlet) {
		this.servlet = servlet;
	}

	public static Datastore initDataStore(GenericServlet servlet) {
		try {
			URL theurl = servlet.getServletContext().getResource(PATH_TO_JSON_KEY);
			File file = new File(theurl.toURI());
			FileInputStream fileInputStreamKey = new FileInputStream(file);

			return DatastoreOptions.builder().projectId(PROJECT_ID)
					.authCredentials(AuthCredentials.createForJson(fileInputStreamKey)).build().service();
		} catch (URISyntaxException | IOException e1) {
			return null;
		}
	}
	
	protected void clearAllEntities(String kind) {
		
		Datastore datastore = initDataStore(servlet);
		Query<Entity> query = Query.entityQueryBuilder().kind(kind).build();
		Iterator<Entity> iterator = datastore.run(query);
		
		// collect keys
		ArrayList<Key> keys = new ArrayList<>();
		while (iterator.hasNext()) {
			keys.add(iterator.next().key());
		}
		
		// delete
		datastore.delete(keys.toArray(new Key[keys.size()] ));
	}

}
