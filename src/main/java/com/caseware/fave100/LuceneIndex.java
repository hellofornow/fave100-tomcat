package com.caseware.fave100;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndex {
	public static StandardAnalyzer ANALYZER;
	public static IndexSearcher SEARCHER;

	static {
		ANALYZER = new StandardAnalyzer(Version.LUCENE_42, new CharArraySet(Version.LUCENE_42, 0, true));

		final File file = new File(Thread.currentThread().getContextClassLoader().getResource("lucene-index").getPath());

		try {
			final Directory index = FSDirectory.open(file);
			final IndexReader reader = DirectoryReader.open(index);
			SEARCHER = new IndexSearcher(reader);
		}
		catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

}
