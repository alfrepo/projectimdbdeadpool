package de.projectimdbdeadpool.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.GenericServlet;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

import de.projectimdbdeadpool.model.FilmUrl;

public class UtilStoreGcloudFilmUrl extends UtilStoreGcloud {
	
	private static final Logger log = Logger.getLogger(UtilStoreGcloudFilmUrl.class.getName());
	
	protected static final String FILM_URL_KEY = "FilmUrl";

	public UtilStoreGcloudFilmUrl(GenericServlet servlet) {
		super(servlet);
	}

	public Key storeFilmUrl(FilmUrl filmUrl) throws IOException {
		
		if (filmUrl == null || !filmUrl.isValid()) {
			return null;
		}
		
		Datastore datastore = initDataStore(servlet);
		
		// Create a Key factory to construct keys associated with this project.
		KeyFactory keyFactory = datastore.newKeyFactory().setKind(FILM_URL_KEY);
		
	
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity dataEntity = Entity.newBuilder(key)
				.set("created", UtilsConvert.toTimeStamp(filmUrl.created))
				.set("name", StringValue.newBuilder(filmUrl.name).build())
				.set("imdbUrl", StringValue.newBuilder(filmUrl.imdbUrl).build())
				.build();
	
		datastore.put(dataEntity);
		
		return key;
	}

	public List<FilmUrl> loadFilmUrl() {

			Datastore datastore = initDataStore(servlet);
			
			Query<Entity> query = Query.newEntityQueryBuilder()
					.setKind(FILM_URL_KEY)
					.addOrderBy(OrderBy.asc("created"))
					.build();
			Iterator<Entity> iterator = datastore.run(query);
	
			List<FilmUrl> list = new ArrayList<>();
			while(iterator.hasNext()){
				Entity entity = iterator.next();
				
				FilmUrl data = new FilmUrl();
				data.created = UtilsConvert.toDateTime(entity.getTimestamp("created"));
				data.name = entity.getString("name");
				data.imdbUrl = entity.getString("imdbUrl");
				data.entityKey = entity.getKey();
				
				list.add(data);
			}
			return list;
	}
	
	public void deleteFilmUrl(String nameOrId) {

		Datastore datastore = initDataStore(servlet);
		Query<Entity> query = Query.newEntityQueryBuilder().setKind(FILM_URL_KEY).build();
		Iterator<Entity> iterator = datastore.run(query);
		
		log.info("Search nameOrId "+nameOrId);
		
		// iterate keys
		while (iterator.hasNext()) {
			Entity e = iterator.next();
			log.info("Url nameOrId "+e.getKey().getNameOrId());
			
			if(e.getKey().getNameOrId().toString().equals(nameOrId)){
				
				log.info("Found nameOrId "+nameOrId+". Will delete now.");
				
				// delete
				datastore.delete(e.getKey());
				return;
			}
		}
	}

	public void clearAllFilmUrlFromDatastore() {
		clearAllEntities(servlet, FILM_URL_KEY);
	}

}
