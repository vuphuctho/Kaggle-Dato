import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

public class ContentSimilarity {
	private final String CONTENTFIELD = "CONTENT";
	private HashSet<String> terms = new HashSet<String>();;
	private RealVector v1;
	private RealVector v2;
	
	public ContentSimilarity(String str1, String str2){
		try {
			Directory directory = createIndex(str1, str2);
			IndexReader reader =  DirectoryReader.open(directory);
			Map<String, Integer> f1 = getTermFrequency(reader, 0);
			Map<String, Integer> f2 = getTermFrequency(reader, 1);
			reader.close();
			v1 = toRealVector(f1);
			v2 = toRealVector(f2);
		} catch (IOException e) {
			e.printStackTrace();
	    } 
	}
	
	private RealVector toRealVector(Map<String, Integer> map) {
		RealVector vector = new ArrayRealVector(terms.size());
		int i=0;
		for (String term : terms) {
			int value = map.containsKey(term)? map.get(term) : 0;
			vector.addToEntry(i, value);
			i++;
		}
		return vector;
	}

	private Map<String, Integer> getTermFrequency(IndexReader reader, int i) {
		try {
			Terms vector = reader.getTermVector(i, CONTENTFIELD);
			Map<String, Integer> result = new HashMap<String, Integer>();
			TermsEnum termsEnum = vector.iterator();
			BytesRef text = null;
			while ((text = termsEnum.next())!=null) {
				String term = text.utf8ToString();
				int freq = (int) termsEnum.totalTermFreq();
				result.put(term, freq);
				terms.add(term);
			}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}

	private Directory createIndex(String str1, String str2) throws IOException {
		RAMDirectory directory = new RAMDirectory();
		Analyzer analyzer = new CustomAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		addDocument(writer, str1);
		addDocument(writer, str2);
		writer.close();
		analyzer.close();
		return directory;
	}

	/* indexed, tokenized, stored with term vectors */
	private void addDocument(IndexWriter writer, String str) throws IOException {
		Document doc = new Document();
		FieldType field = new FieldType();
		field.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		field.setStored(true);
		field.setTokenized(true);
		field.setStoreTermVectors(true);
		doc.add(new Field(CONTENTFIELD, str, field));
		writer.addDocument(doc);
	}
	
	/*
	 * Calculate similarity between 2 input texts
	 */
	public void calcSimilarity() {
		double sim = (v1.dotProduct(v2))/(v1.getNorm() * v2.getNorm());
		System.out.printf("Similarity = %f\n", sim);
	}
	
	// Just for testing
	public static void main(String[] args) {
		String text1 = "Something is here. Testing it out!!";
		String text2 = "Something is here. Test it out now!!";
		String text3 = "Finish testing. Show result, don't return sh*t!";
		ContentSimilarity cs = new ContentSimilarity(text1, text3);
		
		cs.calcSimilarity();
	} 
}