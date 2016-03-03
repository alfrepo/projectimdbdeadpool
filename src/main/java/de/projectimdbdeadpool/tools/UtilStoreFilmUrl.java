package de.projectimdbdeadpool.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.GenericServlet;

import com.google.gcloud.datastore.Datastore;
import com.google.gcloud.datastore.Entity;
import com.google.gcloud.datastore.Key;
import com.google.gcloud.datastore.KeyFactory;
import com.google.gcloud.datastore.Query;
import com.google.gcloud.datastore.StringValue;
import com.google.gcloud.datastore.StructuredQuery.OrderBy;

import de.projectimdbdeadpool.model.FilmUrl;
import de.projectimdbdeadpool.servlet.AllFilmDataServlet;

public class UtilStoreFilmUrl extends UtilStore {
	
	private static final Logger log = Logger.getLogger(UtilStoreFilmUrl.class.getName());
	
	protected static final String FILM_URL_KEY = "FilmUrl";

	public UtilStoreFilmUrl(GenericServlet servlet) {
		super(servlet);
	}

	public Key storeFilmUrl(FilmUrl filmUrl) throws IOException {
		if (filmUrl == null || !filmUrl.isValid()) {
			return null;
		}
		
		Datastore datastore = initDataStore(servlet);
		
		// Create a Key factory to construct keys associated with this project.
		KeyFactory keyFactory = datastore.newKeyFactory().kind(FILM_URL_KEY);
		
	
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity dataEntity = Entity.builder(key)
				.set("created", filmUrl.created)
				.set("name", StringValue.builder(filmUrl.name).indexed(true).build())
				.set("imdbUrl", StringValue.builder(filmUrl.imdbUrl).indexed(true).build())
				.build();
	
		datastore.put(dataEntity);
		
		return key;
	}

	public List<FilmUrl> loadFilmUrl() {
			Datastore datastore = initDataStore(servlet);
			
			Query<Entity> query = Query.entityQueryBuilder()
					.kind(FILM_URL_KEY)
					.orderBy(OrderBy.asc("created"))
					.build();
			Iterator<Entity> iterator = datastore.run(query);
	
			List<FilmUrl> list = new ArrayList<>();
			while(iterator.hasNext()){
				Entity entity = iterator.next();
				
				FilmUrl data = new FilmUrl();
				data.created = entity.getDateTime("created");
				data.name = entity.getString("name");
				data.imdbUrl = entity.getString("imdbUrl");
				data.entityKey = entity.key();
				
				list.add(data);
			}
			return list;
	}
	
	public void deleteFilmUrl(String nameOrId) {
		Datastore datastore = initDataStore(servlet);
		Query<Entity> query = Query.entityQueryBuilder().kind(FILM_URL_KEY).build();
		Iterator<Entity> iterator = datastore.run(query);
		
		log.info("Search nameOrId "+nameOrId);
		
		// iterate keys
		while (iterator.hasNext()) {
			Entity e = iterator.next();
			log.info("Url nameOrId "+e.key().nameOrId());
			
			if(e.key().nameOrId().toString().equals(nameOrId)){
				
				log.info("Found nameOrId "+nameOrId+". Will delete now.");
				
				// delete
				datastore.delete(e.key());
				return;
			}
		}
	}

	public void clearAllFilmUrlFromDatastore() {
		clearAllEntities(FILM_URL_KEY);
	}

}
