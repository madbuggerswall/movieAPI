public class Movie {

	String title;
	int releaseYear;
	int duration;
	Director director;

	public Movie() {
	}

	public Movie(String title) {
		this.title = title;
	}

	public Movie(String title, int releaseYear) {
		this(title);
		this.releaseYear = releaseYear;
	}

	public Movie(String title, int releaseYear, int duration) {
		this(title, releaseYear);
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public int getDuration() {
		return duration;
	}

	public int getReleaseYear() {
		return releaseYear;
	}
}