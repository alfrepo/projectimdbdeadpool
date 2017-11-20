package de.projectimdbdeadpool.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.StructuredQuery.Filter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import de.projectimdbdeadpool.tools.UtilStoreGcloud;

public class QueryDbExamplesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		
		Datastore datastore = UtilStoreGcloud.initDataStore(this);
		
		Filter ratingFilter =	PropertyFilter.eq("rating", 8.5);
		Filter ratingCntFilter = PropertyFilter.ge("ratingCnt", 100);
		Filter imdbUrlFilter =	PropertyFilter.eq("imdbUrl", "http://www.imdb.com/title/tt1431045/");
		
		Query<Entity> query = null;
		// TODO rewrite
//		Query<Entity> query = Query.entityQueryBuilder()
//				.kind("FilmData")
//				.filter(imdbUrlFilter)
//				.orderBy(OrderBy.desc("created"))
//				.build();
		
		Iterator<Entity> iterator = datastore.run(query);
		
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
	}
}
