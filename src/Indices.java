//class to store content and title similarity index, used for sorting

public class Indices {

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
		
//	@Override
	public int compareTo(Indicies other) {
		return new Double(this.getContentCS()).compareTo(other.getContentCS());
	}
}
