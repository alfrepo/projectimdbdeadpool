package de.projectimdbdeadpool.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.GenericServlet;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DoubleValue;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.EntityQuery.Builder;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.Filter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import de.projectimdbdeadpool.model.FilmData;

public class UtilStoreGcloudFilmData extends UtilStoreGcloud {
	
	protected static final String FILM_DATA_KEY = "FilmData";
	
	private static final Logger log = Logger.getLogger(UtilStoreGcloudFilmData.class.getName());

	public UtilStoreGcloudFilmData(GenericServlet servlet) {
		super(servlet);
	}

	public Key storeFilmData(FilmData filmData) throws IOException {
		if (filmData == null || !filmData.isValid()) {
			return null;
		}
		
		Datastore datastore = initDataStore(servlet);
		
	
		// Create a Key factory to construct keys associated with this project.
		KeyFactory keyFactory = datastore.newKeyFactory().setKind(FILM_DATA_KEY);
	
		Key key = datastore.allocateId(keyFactory.newKey());
		Entity dataEntity = Entity.newBuilder(key)
				.set("created", UtilsConvert.toTimeStamp(filmData.created))
				.set("name", StringValue.newBuilder(filmData.name).build())
				.set("rating", DoubleValue.newBuilder(UtilsConvert.toDouble(filmData.imdbRating)).build())
				.set("ratingCnt", UtilsConvert.toDouble(filmData.imdbRatingCnt))
				.set("imdbUrl", StringValue.newBuilder(filmData.imdbUrl).build())
				.build();
	
		datastore.put(dataEntity);
		return key;
	}

	public List<FilmData> loadFilmData() {
		return loadFilmData(null);
	}

	public List<FilmData> loadFilmData(String imdbId) {
			Datastore datastore = initDataStore(servlet);
			
			Builder queryBuilder = Query.newEntityQueryBuilder().setKind(FILM_DATA_KEY);
			
			// filter if imdbId is given
			if(imdbId!=null && !imdbId.isEmpty()){
				String imdbUrl = String.format("http://www.imdb.com/title/%s/", imdbId.replace("/", ""));
				Filter imdbUrlFilter =	PropertyFilter.eq("imdbUrl", imdbUrl);
				queryBuilder.setFilter(imdbUrlFilter);
			}
			
			
			// sort
			queryBuilder.addOrderBy(OrderBy.asc("created"));
			
			EntityQuery query = queryBuilder.build();
			QueryResults<Entity> iterator = datastore.run(query);
	
			List<FilmData> list = new ArrayList<>();
			while(iterator.hasNext()){
				Entity entity = iterator.next();
				FilmData data = new FilmData();
				
				data.created = UtilsConvert.toDateTime(entity.getTimestamp("created"));
				data.name = entity.getString("name");
				data.imdbRating = Double.toString(entity.getDouble("rating"));
				data.imdbRatingCnt = Double.toString(entity.getDouble("ratingCnt"));
				data.imdbUrl = entity.getString("imdbUrl");
				data.entityKey = entity.getKey();
				
				list.add(data);
			}
			
			return list;
		}

	public void clearAllFilmDataFromDatastore() {
		clearAllEntities(servlet, FILM_DATA_KEY);
	}
	


}
