import java.io.FileInputStream;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

class DatabaseManager {
	static final String movieCollectionName = "Movies";
	static final String userCollectionName = "Users";

	Firestore db;
	CollectionReference movies;
	CollectionReference users;

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
	}

	// Returns a null object if there is no object with given document ID.
	public Movie getMovie(String docID) {
		DocumentSnapshot document = getDocument(docID, movies);
		Movie movie = null;
		if (document.exists())
			movie = document.toObject(Movie.class);
		else
			System.out.println("No such document!");

		return movie;
	}

	public User getUser(String docID) {
		DocumentSnapshot document = getDocument(docID, users);
		User user = null;
		if (document.exists())
			user = document.toObject(User.class);
		else
			System.out.println("No such document!");

		return user;
	}

	public void addMovie(Movie movie) {
		addDocument(movie.title, movies, movie);		
	}

	public void addUser(User user) {
		addDocument(user.username, users, user);
	}

	// Tries to set Google Credentials from a file stream. Sets it to null if stream fails.
	GoogleCredentials getGoogleCredentials(String filePath) {
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
	FileInputStream getCredentialsStream(String filePath) {
		FileInputStream credentialsStream = null;
		try {
			credentialsStream = new FileInputStream(filePath);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return credentialsStream;
	}

	// future.get() blocks on response.
	DocumentSnapshot getDocument(String docID, CollectionReference collection) {
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

	// Add a new document (asynchronously) in collection with id.
	void addDocument(String docID, CollectionReference collection, Object object) {
		ApiFuture<WriteResult> future = collection.document(docID).set(object);

		try {
			System.out.println("Update time : " + future.get().getUpdateTime());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
