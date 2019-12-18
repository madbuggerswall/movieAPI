import java.util.List;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;

import org.apache.commons.codec.digest.DigestUtils;

class Database {
	private static Database instance;
	private DatabaseManager dbManager;

	private static final String movieCollectionName = "Movies";
	private static final String directorCollectionName = "Directors";
	private static final String userCollectionName = "Users";

	private CollectionReference movies;
	private CollectionReference users;
	private CollectionReference directors;

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

	// Movie
	public void addMovie(Movie movie) {
		dbManager.addDocument(movies, movie);
		Director director = getDirector(movie.getDirector().getID());
		if(director.getMovies().size() == 1){
			if(director.getMovies().get(0).getID() == null){
				director.getMovies().remove(0);
			}
		}
		director.addMovie(movie);
		setDirector(director);
	}

	public void deleteMovie(String docID) {
		Movie movie = getMovie(docID);
		dbManager.deleteDocument(movies, docID);

		Director director = movie.getDirector();
		if (director.id != null) {
			director.removeMovie(movie);
			if(director.getMovies().isEmpty()){
				director.addMovie(new NullMovie());
			}
			setDirector(director);
		}
	}

	public Movie getMovie(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, movies);
		Movie movie = dbManager.toObject(document, Movie.class);
		return movie;
	}

	public List<Movie> getAllMovies() {
		return dbManager.getCollection(movies, Movie.class);
	}

	public void setMovie(Movie movie) {
		dbManager.setDocument(movies, movie);
	}

	// Director
	public void addDirector(Director director) {
		dbManager.addDocument(directors, director);
	}

	public void deleteDirector(String docID) {
		List<Movie> movies = getDirector(docID).getMovies();
		for (Movie movie : movies) {
			movie.director = new NullDirector();
			setMovie(movie);
		}
		dbManager.deleteDocument(directors, docID);
	}

	public Director getDirector(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, directors);
		Director director = dbManager.toObject(document, Director.class);
		return director;
	}

	public List<Director> getAllDirectors() {
		return dbManager.getCollection(directors, Director.class);
	}

	public void setDirector(Director director) {
		dbManager.setDocument(directors, director);
	}

	public void setDirector(DirectorDTO directorDTO) {
		Director director = getDirector(directorDTO.getID());
		director.name = directorDTO.name;
		director.surname = directorDTO.surname;
		director.birthDate = directorDTO.birthDate;
		dbManager.setDocument(directors, director);
	}

	// User
	public void addUser(User user) {
		user.password = DigestUtils.sha256Hex(user.password);
		dbManager.addDocument(users, user);
	}

	public void addMovieList(String userID, String listName) {
		MovieList movieList = new MovieList(listName);
		User user = getUser(userID);
		user.addList(movieList);
		setUser(user);
	}

	public void addMovieToUserList(String userID, int listIndex, String movieID) {
		User user = getUser(userID);
		Movie movie = getMovie(movieID);
		user.getList(listIndex).addMovie(movie);
		setUser(user);
	}

	public void deleteUser(String docID) {
		dbManager.deleteDocument(users, docID);
	}

	public User findUser(String username) {
		DocumentSnapshot document = dbManager.findDocuments("username", username, users).get(0);
		User user = document.toObject(User.class);
		return user;
	}

	public User getUser(String docID) {
		DocumentSnapshot document = dbManager.getDocument(docID, users);
		User user = dbManager.toObject(document, User.class);
		return user;
	}

	public List<User> getAllUsers() {
		return dbManager.getCollection(users, User.class);
	}

	public User hasLoggedIn(String docID, String accessToken) {
		User user = getUser(docID);
		if (user.accessToken.equals(accessToken))
			return user;
		else
			return null;
	}

	public User loginUser(UserDTO userDTO) {
		if (userNameExists(userDTO.username)) {
			User user = findUser(userDTO.username);
			String userDTOPassword = DigestUtils.sha256Hex(userDTO.password);
			System.out.println(userDTOPassword);
			System.out.println(user.password);
			if (userDTOPassword.equals(user.password)) {
				System.out.println("passwords same");
				user.accessToken = DigestUtils.sha256Hex(userDTO.username + userDTO.password);
				setUser(user);
				return user;
			}
		}
		return null;
	}

	public void logOutUser(String docID) {
		User user = getUser(docID);
		user.accessToken = "";
		setUser(user);
	}

	public void setUser(User user) {
		dbManager.setDocument(users, user);
	}

	public boolean userNameExists(String username) {
		return !dbManager.findDocuments("username", username, users).isEmpty();
	}
}
