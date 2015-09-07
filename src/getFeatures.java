

import java.io.*;
import java.util.*;

public class getFeatures{

	//get 6 features from main content and 3 most unrelated links (title + content)

	public static List<Double> getFeatures(String html) {

		HTMLdata obj = HTMLparser.parse(html);
		String title = obj.getTitle();
		String content = obj.getContent();
		ArrayList<ArrayList<String>> allLinks = obj.getAllLinks();

		Indices[] titleAndContentSim = new Indices[allLinks.size()];

		for (int i = 0; i < allLinks.size(); i++) {

			//System.out.println("link " + i);

			ArrayList<String> this_link = allLinks.get(i);
			ContentSimilarity title_cs = new ContentSimilarity(title, HTMLparser.getPageTitle(HTMLdata.getHTML(this_link)));			 
			ContentSimilarity content_cs = new ContentSimilarity(content, HTMLdata.getContent(this_link));
			
			titleAndContentSim[i] = new Indices(content_cs.calcSimilarity(), title_cs.calcSimilarity());
		}
		Arrays.sort(titleAndContentSim);

		//output the bottom 3 (and the respective title index)
		ArrayList<Double> output = new ArrayList<Double>();
		for (int i = 0; i < 2; i++) {
			output.add(titleAndContentSim[i].getContentCS());
			output.add(titleAndContentSim[i].getTitleCS());
		}

		return output;

	}


}
