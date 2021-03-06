package com.caseware.fave100;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.musicbrainz.search.LuceneVersion;

@Path("/lookup")
public class LookupService {

	@GET
	@Produces("text/plain")
	public String getClichedMessage(@QueryParam("id") final String id) {
		if (id == null || id.isEmpty()) {
			return "Id required";
		}

		try {
			final long startTime = System.currentTimeMillis();
			final Query q = new QueryParser(LuceneVersion.LUCENE_VERSION, "id", LuceneIndex.LOOKUP_ANALYZER).parse(id);

			final TopDocs results = LuceneIndex.SEARCHER.search(q, 1);

			final long endTime = System.currentTimeMillis();
			final long executionTime = endTime - startTime;

			if (results.totalHits == 0)
				return "";

			final ScoreDoc[] hits = results.scoreDocs;
			final int docId = hits[0].doc;
			final Document d = LuceneIndex.SEARCHER.doc(docId);

			final StringBuilder sb = new StringBuilder();
			sb.append("{\"song\":\"" + d.get("song").replaceAll("\"", "") + "\"");
			sb.append(",\"artist\":\"" + d.get("artist").replaceAll("\"", "") + "\"");
			sb.append("}");

			return sb.toString();

		}
		catch (final ParseException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		catch (final IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}
}
