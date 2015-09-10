

import java.io.*;
import java.util.*;

public class getFeatures{

	//get 6 features from main content and 3 most unrelated links (title + content)

	public static List<Double> getFeatures(String fieDirectory) {
		String html = null;
		try {
			html = HTMLparser.readFile(fieDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HTMLdata obj = HTMLparser.parse(html);
		String title = obj.getTitle();
		String content = obj.getContent();
		ArrayList<ArrayList<String>> allLinks = obj.getAllLinks();

		Indices[] titleAndContentSim = new Indices[allLinks.size()];

		for (int i = 0; i < allLinks.size(); i++) {
			ArrayList<String> this_link = allLinks.get(i);
			ContentSimilarity title_cs = new ContentSimilarity(title, HTMLparser.getPageTitle(HTMLdata.getHTML(this_link)));			 
			ContentSimilarity content_cs = new ContentSimilarity(content, HTMLdata.getContent(this_link));
			
			titleAndContentSim[i] = new Indices(content_cs.calcSimilarity(), title_cs.calcSimilarity());
		}
		Arrays.sort(titleAndContentSim);

		//output the bottom 3 (and the respective title index)
		ArrayList<Double> output = new ArrayList<Double>();
		
		for (int i = 0; i < 3; i++) {
			if (i<titleAndContentSim.length) {
				output.add(titleAndContentSim[i].getContentCS());
				output.add(titleAndContentSim[i].getTitleCS());
			} else {  // not enough input values
				output.add(0.0); output.add(0.0); 
			}
		}

		return output;

	}

	
	// test getFeature
	public static void main(String[] args) {
		String file = "Data//htmls//26_raw_html.txt";
		List<Double> vars = getFeatures.getFeatures(file);
		for (Double var : vars) {
			System.out.println(var);
		}
	}
	

}
