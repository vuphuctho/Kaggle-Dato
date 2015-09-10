
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
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

/*
 * Compare similarity between two input documents:
 * - Apply customized filtering on documents
 * - Compare using Cosine similarity 
 */
public class ContentSimilarity {
	private final String CONTENTFIELD = "CONTENT";
	private HashSet<String> terms = new HashSet<String>();;
	private RealVector v1;
	private RealVector v2;
	
	/* CONSTRUCTOR */
	public ContentSimilarity(String str1, String str2){
		try {
			// temp sol for null input
			if (str1==null|| str1.length()==0) str1="null";
			if (str2==null|| str2.length()==0) str2="null";
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
	
	/*
	 * Convert map structure to RealVector for future use
	 */
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

	/*
	 * Extract term frequency of document at index i in directory
	 */
	private Map<String, Integer> getTermFrequency(IndexReader reader, int i) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			Terms vector = reader.getTermVector(i, CONTENTFIELD);
			TermsEnum termsEnum = vector.iterator();
			BytesRef text = null;
			while ((text = termsEnum.next())!=null) {
				String term = text.utf8ToString();
				int freq = (int) termsEnum.totalTermFreq();
				result.put(term, freq);
				terms.add(term);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

	/*
	 * Write 2 input documents into memory
	 */
	private Directory createIndex(String str1, String str2) throws IOException {
		RAMDirectory directory = new RAMDirectory();
		Analyzer analyzer = new CustomizedAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		addDocument(writer, str1);
		addDocument(writer, str2);
		writer.close();
		analyzer.close();
		return directory;
	}

	/*
	 * Index, tokenize and store with term vectors
	 */
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
	 * using Cosine similarity
	 */
	public double calcSimilarity() {
		double sim = (v1.dotProduct(v2))/(v1.getNorm() * v2.getNorm());
		return sim;
	}
	
	// Just for testing
	public static void main(String[] args) {
		String text1 = "Something is here. Testing it out!!";
		String text2 = "Something is here. Test it out now!!";
		String text3 = "";
		ContentSimilarity cs = new ContentSimilarity(text1, text3);
		
		System.out.println(cs.calcSimilarity());
	} 
}
