import java.util.ArrayList;
import java.util.List;

public class MovieList {
	String name;
	List<Movie> movies;

	public MovieList() {
		movies = new ArrayList<Movie>();
	}

	public MovieList(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void addMovie(Movie movie) {
		movies.add(movie);
	}

	public void removeMovie(Movie movie) {
		movies.remove(movie);
	}

	void sortList() {

	}
}