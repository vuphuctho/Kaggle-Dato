import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;


public class CustomizedAnalyzer extends Analyzer {
	/*
	 * Remove stop words and apply Porter Stemming 
	 */
	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		Tokenizer source = new LetterTokenizer();
	    TokenStream filter = new LowerCaseFilter(source);
	    filter = new StopFilter(filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	    filter = new PorterStemFilter(filter);
	    return new TokenStreamComponents(source, filter);
	}
	
	// test standardAnalyzer
	public static void main(String[] args) throws IOException {
		String str = "Testing things to see if analyzer works effectively.";
		Analyzer analyzer = new CustomizedAnalyzer();
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
