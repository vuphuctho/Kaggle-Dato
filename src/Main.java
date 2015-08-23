import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;


public class Main {
	// test standardAnalyzer
	public static void main(String[] args) throws IOException {
		String str = "Testing things to see if analyzer works effectively.";
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		Document doc = new Document();
		doc.add((IndexableField) new Field("fieldname", str, TextField.TYPE_STORED));
		iwriter.addDocument(doc);
		iwriter.close();
		
		TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(str));
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
		    String term = charTermAttribute.toString();
		    System.out.println(term);
		}
		return;
	}
}
