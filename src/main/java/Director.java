import java.util.ArrayList;
import java.util.List;

public class Director extends Data {
	String name;
	List<Movie> movies;

	public Director() {
		movies = new ArrayList<Movie>();
	}

	public Director(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Movie> getMovies() {
		return movies;
	}

}