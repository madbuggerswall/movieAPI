import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

class DatabaseManager {
	static DatabaseManager instance;

	static final String credentialsFilePath = "credentials.json";

	static final String movieCollectionName = "Movies";
	static final String userCollectionName = "Users";
	static final String directorsCollectionName = "Directors";

	Firestore db;
	CollectionReference movies;
	CollectionReference users;
	CollectionReference directors;

	// Assert singleton.
	public static DatabaseManager getInstance() {
		if (instance == null) {
			return new DatabaseManager();
		}
		return instance;
	}

	public DatabaseManager() {
		this(credentialsFilePath);
	}

	// Creates the database instance.
	public DatabaseManager(String credentialsFilePath) {
		GoogleCredentials googleCredentials = getGoogleCredentials(credentialsFilePath);
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(googleCredentials)
			.build();
		FirebaseApp.initializeApp(options);

		db = FirestoreClient.getFirestore();
		movies = db.collection(movieCollectionName);
		users = db.collection(userCollectionName);
		directors = db.collection(directorsCollectionName);
	}

	public void addMovie(Movie movie) {
		addDocument(movies, movie);
		Director director = getDirector(movie.director.getID());
		director.addMovie(movie);
		setDirector(director);
	}

	public void addDirector(Director director) {
		addDocument(directors, director);
	}

	public void addUser(User user) {
		addDocument(users, user);
	}

	// Returns a null object if there is no object with given document ID.
	public Movie getMovie(String docID) {
		DocumentSnapshot document = getDocument(docID, movies);
		Movie movie = toObject(document, Movie.class);
		return movie;
	}

	public Director getDirector(String docID) {
		DocumentSnapshot document = getDocument(docID, directors);
		Director director = toObject(document, Director.class);
		return director;
	}

	public User getUser(String docID) {
		DocumentSnapshot document = getDocument(docID, users);
		User user = toObject(document, User.class);
		return user;
	}

	public List<Movie> getAllMovies() {
		return getCollection(movies, Movie.class);
	}

	public List<Director> getAllDirectors() {
		return getCollection(directors, Director.class);
	}

	public List<User> getAllUsers() {
		return getCollection(users, User.class);
	}

	public void setMovie(Movie movie) {
		setDocument(movies, movie);
	}

	public void setDirector(Director director) {
		setDocument(directors, director);
	}

	public void setUser(User user) {
		setDocument(users, user);
	}

	// Possibly redundant find() methods.
	public List<Movie> findMovies(String title) {
		return findDocuments(movies, Movie.class, "title", title);
	}

	public List<Director> findDirectors(String name) {
		return findDocuments(directors, Director.class, "name", name);
	}

	public List<User> findUsers(String username) {
		return findDocuments(users, User.class, "username", username);
	}

	// Add a new document with auto-generated ID.
	private void addDocument(CollectionReference collection, Data data) {
		DocumentReference docRef = collection.document();
		data.setID(docRef.getId());
		ApiFuture<WriteResult> future = docRef.set(data);
		System.out.println(data.id);
		try {
			System.out.println("Update time : " + future.get().getUpdateTime());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// future.get() blocks on response.
	private DocumentSnapshot getDocument(String docID, CollectionReference collection) {
		System.out.println(docID);
		DocumentReference docRef = collection.document(docID);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = null;
		try {
			document = future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	private <T> List<T> getCollection(CollectionReference collection, Class<T> valueType) {
		List<T> objects = new ArrayList<T>();
		ApiFuture<QuerySnapshot> future = collection.get();
		List<QueryDocumentSnapshot> documents = new ArrayList<QueryDocumentSnapshot>();

		try {
			documents = future.get().getDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (DocumentSnapshot document : documents) {
			objects.add(document.toObject(valueType));
		}

		return objects;
	}

	private void setDocument(CollectionReference collection, Data data) {
		DocumentReference docRef = collection.document(data.id);
		ApiFuture<WriteResult> future = docRef.set(data);

		try {
			System.out.println("Update time : " + future.get().getUpdateTime());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private <T> List<T> findDocuments(CollectionReference collection, Class<T> valueType, String field, String value) {
		List<T> foundObjects = new ArrayList<T>();

		Query query = collection.whereEqualTo(field, value);
		List<QueryDocumentSnapshot> documents = new ArrayList<QueryDocumentSnapshot>();
		ApiFuture<QuerySnapshot> future = query.get();

		try {
			documents = future.get().getDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (DocumentSnapshot document : documents) {
			foundObjects.add(document.toObject(valueType));
		}

		return foundObjects;
	}

	private <T> T toObject(DocumentSnapshot document, Class<T> valueType) {
		T object = null;
		if (document.exists()) {
			object = document.toObject(valueType);
		} else {
			System.out.println("No such document!");
		}
		return object;
	}

	// Tries to set Google Credentials from a file stream. Sets it to null if stream fails.
	private GoogleCredentials getGoogleCredentials(String filePath) {
		FileInputStream credentialsStream = getCredentialsStream(filePath);
		GoogleCredentials googleCredentials = null;
		try {
			googleCredentials = GoogleCredentials.fromStream(credentialsStream);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return googleCredentials;
	}

	// Tries to get credential file stream. Sets it to null if file not found.
	private FileInputStream getCredentialsStream(String filePath) {
		FileInputStream credentialsStream = null;
		try {
			credentialsStream = new FileInputStream(filePath);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return credentialsStream;
	}
}
