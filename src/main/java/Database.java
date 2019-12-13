import java.util.List;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;

class Database {
	static Database instance;
	DatabaseManager dbManager;

	static final String movieCollectionName = "Movies";
	static final String directorCollectionName = "Directors";
	static final String userCollectionName = "Users";

	CollectionReference movies;
	CollectionReference users;
	CollectionReference directors;

	// Assert singleton.
	public static Database getInstance() {
		if (instance == null) {
			return new Database();
		}
		return instance;
	}

	public Database() {
		dbManager = DatabaseManager.getInstance();
		movies = dbManager.getCollectionReference(movieCollectionName);
		directors = dbManager.getCollectionReference(directorCollectionName);
		users = dbManager.getCollectionReference(userCollectionName);
	}

	public void addMovie(Movie movie) {
		dbManager.addDocument(movies, movie);
		Director director = getDirector(movie.director.getID());
		director.addMovie(movie);
		setDirector(director);
	}

	public void addDirector(Director director) {
		dbManager.addDocument(directors, director);
	}

	public void addUser(User user) {
		dbManager.addDocument(users, user);
	}

	// Returns a null object if there is no object with given document ID.
	public Movie getMovie(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, movies);
		Movie movie = dbManager.toObject(document, Movie.class);
		return movie;
	}

	public Director getDirector(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, directors);
		Director director = dbManager.toObject(document, Director.class);
		return director;
	}

	public User getUser(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, users);
		User user = dbManager.toObject(document, User.class);
		return user;
	}

	public List<Movie> getAllMovies() {
		return dbManager.getCollection(movies, Movie.class);
	}

	public List<Director> getAllDirectors() {
		return dbManager.getCollection(directors, Director.class);
	}

	public List<User> getAllUsers() {
		return dbManager.getCollection(users, User.class);
	}

	public void setMovie(Movie movie) {
		dbManager.setDocument(movies, movie);
	}

	public void setDirector(Director director) {
		dbManager.setDocument(directors, director);
	}

	public void setUser(User user) {
		dbManager.setDocument(users, user);
	}
}

