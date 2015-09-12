
import java.util.*;
import java.io.*;
import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLparser{
	
	//get title, hyperlink, content, all adjacent tags
	public static HTMLdata parse (String html) {
	    
	    HTMLdata result = new HTMLdata();

	    Document doc = Jsoup.parse(html);

	    //get title and content
	    Elements links = doc.select("a");
	    Element title = doc.select("title").first();
	    Element body = doc.select("body").first();
	    String title_text = doc.title();
	    String body_text = body.text();

	    //modify final output
	    result.addTitle(title_text);
	    result.addContent(body_text);
	    
	    for (Element e : links) {

		//get content, link of the hyperlink
		String content_link = e.text();
		String link = e.attr("href");
		//System.out.println("LINK " + link + ", CONTENT: " + content_link);
	
		//get all adjacent tags
		Set<String> tags = new HashSet<String>();
		Elements siblings = e.siblingElements();
		for (Element s : siblings) {
			tags.add(s.tagName());
			}
		Element parent = e.parent();
		tags.add(parent.tagName());
		Elements children = e.children();
		for (Element c : children) {
			tags.add(c.tagName());
			}

		//convert tags to String, separated by comma
		String final_tags = "";
		Iterator<String> it = tags.iterator();
		while (it.hasNext()) {
			final_tags += it.next() + ",";
		}

		//modify final output
		ArrayList<String> new_link = HTMLdata.createLink(link, content_link, final_tags);
		result.addLink(new_link);
		}

	     //print out
	     return result;
	}

	public static String readFile(String input) throws IOException {
		File file = new File(input);
		Scanner sc = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder fileContent = new StringBuilder((int) file.length());
		try {
			while(sc.hasNextLine()) {        
			    fileContent.append(sc.nextLine() + lineSeparator);
			}
			return fileContent.toString();
	        } 
		finally {
			sc.close();
		}
	}

	// get page title from input URL
	public static String getPageTitle(String url) {
		if (url.length() > 4 && url.substring(0, 4).compareTo("http")==0) {
			try {
				// set timeout to 10 seconds to wait for respond from server
				Document doc = Jsoup.connect(url).userAgent("Mozilla").ignoreContentType(true).timeout(10000).get();
				return doc.title();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "null";
	}

	public static void main(String[] args) {
		
		//TEST		

		String html = null;
		try {
			// for Tue's repo only
			// html = readFile("0/114_raw_html.txt");
			// for Tho's repo only
			html = readFile("Data/htmls/26_raw_html.txt");
		}
		catch (Exception e) {
			System.out.println(e);
		}
		HTMLdata obj = parse(html);
		System.out.println("title: " + obj.getTitle());
		System.out.println("content: " + obj.getContent());
		ArrayList<ArrayList<String>> allLinks = obj.getAllLinks();
		for (int i = 0; i < allLinks.size(); i++) {
			System.out.println("link " + i);
			ArrayList<String> this_link = allLinks.get(i);
			System.out.println("html " + HTMLdata.getHTML(this_link));
			System.out.println("html page title " + getPageTitle(HTMLdata.getHTML(this_link)));
			System.out.println("content " + HTMLdata.getContent(this_link));
			System.out.println("tag " + HTMLdata.getTag(this_link));
		}
	}

	

}
