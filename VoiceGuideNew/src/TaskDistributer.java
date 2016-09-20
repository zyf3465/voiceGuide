import javax.servlet.ServletContext;

import javax.servlet.ServletContext;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;

public class TaskDistributer extends Processor{
	String excute(String recResult) {
		if (recResult.contains("行")) {
			return "task3";
		} 
		return "task2";
	}
}
