//class to store content and title similarity index, used for sorting

public class Indices implements Comparable<Indices>{

	double content;
	double title;

	public Indices(double x, double y) {
			content = x; title = y;
	}

	public double getContentCS() {
		return content;
	}

	public double getTitleCS() {
		return title;
	}
		
	public int compareTo(Indices other) {
		return new Double(this.getContentCS()).compareTo(other.getContentCS());
	}
}