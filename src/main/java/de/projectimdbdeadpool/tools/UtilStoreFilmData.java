package de.projectimdbdeadpool.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.GenericServlet;

import com.google.gcloud.datastore.Datastore;
import com.google.gcloud.datastore.DoubleValue;
import com.google.gcloud.datastore.Entity;
import com.google.gcloud.datastore.Key;
import com.google.gcloud.datastore.KeyFactory;
import com.google.gcloud.datastore.Query;
import com.google.gcloud.datastore.StringValue;
import com.google.gcloud.datastore.StructuredQuery.OrderBy;

import de.projectimdbdeadpool.model.FilmData;

public class UtilStoreFilmData extends UtilStore {
	
	protected static final String FILM_DATA_KEY = "FilmData";

	public UtilStoreFilmData(GenericServlet servlet) {
		super(servlet);
	}

	public Key storeFilmData(FilmData filmData) throws IOException {
		
		if (filmData == null || !filmData.isValid()) {
			return null;
		}
		
		Datastore datastore = initDataStore(servlet);
		
	
		// Create a Key factory to construct keys associated with this project.
		KeyFactory keyFactory = datastore.newKeyFactory().kind(FILM_DATA_KEY);
		
	
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity dataEntity = Entity.builder(key)
				.set("created", filmData.created)
				.set("name", StringValue.builder(filmData.name).indexed(true).build())
				.set("rating", DoubleValue.builder(UtilsConvert.toDouble(filmData.imdbRating)).indexed(true).build())
				.set("ratingCnt", UtilsConvert.toDouble(filmData.imdbRatingCnt))
				.set("imdbUrl", StringValue.builder(filmData.imdbUrl).indexed(true).build())
				.build();
	
		datastore.put(dataEntity);
		return key;
	}

	public List<FilmData> loadFilmData() {
		return loadFilmData(null);
	}

	public List<FilmData> loadFilmData(String imdbId) {
			Datastore datastore = initDataStore(servlet);
			
	//		Filter imdbUrlFilter =	PropertyFilter.eq("imdbUrl", "http://www.imdb.com/title/tt1431045/");
	//		Filter imdbUrlFilter =	PropertyFilter.eq("rating", 8.5);
			
			Query<Entity> query = Query.entityQueryBuilder()
					.kind(FILM_DATA_KEY)
	//				.filter(imdbUrlFilter)
					.orderBy(OrderBy.asc("created"))
					.build();
			Iterator<Entity> iterator = datastore.run(query);
	
			List<FilmData> list = new ArrayList<>();
			while(iterator.hasNext()){
				Entity entity = iterator.next();
				
				FilmData data = new FilmData();
				data.created = entity.getDateTime("created");
				data.name = entity.getString("name");
				data.imdbRating = Double.toString(entity.getDouble("rating"));
				data.imdbRatingCnt = Double.toString(entity.getDouble("ratingCnt"));
				data.imdbUrl = entity.getString("imdbUrl");
				data.entityKey = entity.key();
				
				list.add(data);
			}
			
			// filter
			if(imdbId!=null && !imdbId.isEmpty()){
				Iterator<FilmData> iter =  list.iterator();
				while (iter.hasNext()) {
					if(!iter.next().imdbUrl.endsWith(imdbId+"/")){
						iter.remove();
					}
				}
			}
			
			
			return list;
		}

	public void clearAllFilmDataFromDatastore() {
		clearAllEntities(FILM_DATA_KEY);
	}

}
