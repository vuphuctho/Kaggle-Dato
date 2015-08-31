

import java.util.*;

public class HTMLdata{

	String title, body;
	ArrayList<ArrayList<String>> listOfList = new ArrayList<ArrayList<String>>();

	public HTMLdata() {

	}

	public void addTitle(String t) {
		title = t;
	}
	public void addContent(String c) {
		body = c;
	}
	public void addLink(ArrayList<String> l) {
		listOfList.add(l);
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return body;
	}

	public ArrayList<ArrayList<String>> getAllLinks() {
		return listOfList;
	}

	public ArrayList<String> getLinkByIndex(int index) {
		return listOfList.get(index);
	}

	public static String getHTML(ArrayList<String> link) {
		return link.get(0);
	}

	public static String getContent(ArrayList<String> link) {
		return link.get(1);
	}

	public static String getTag(ArrayList<String> link) {
		return link.get(2);
	}

	public static ArrayList<String> createLink(String link, String content, String tags) {
		ArrayList<String> result = new ArrayList<String>();
		result.add(link);
		result.add(content);
		result.add(tags);
		return result;
	}
}
