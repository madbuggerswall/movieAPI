import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
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

// Generic Firebase operations. Only Database class should access this.
// All operations should be done via Database class.

class DatabaseManager {
	static DatabaseManager instance;
	static final String credentialsFilePath = "credentials.json";

	Firestore db;

	public DatabaseManager() {
		GoogleCredentials googleCredentials = getGoogleCredentials(credentialsFilePath);
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(googleCredentials)
			.build();
		FirebaseApp.initializeApp(options);

		db = FirestoreClient.getFirestore();
	}

	// Assert singleton.
	public static DatabaseManager getInstance() {
		if (instance == null) {
			return new DatabaseManager();
		}
		return instance;
	}

	// Add a new document with auto-generated ID.
	public void addDocument(CollectionReference collection, Data data) {
		DocumentReference docRef = collection.document();
		data.setID(docRef.getId());
		ApiFuture<WriteResult> future = docRef.set(data);
		try {
			Timestamp timestamp = future.get().getUpdateTime();
			LogEntry logEntry = new LogEntry.Builder("[Add]")
				.withID(data.id)
				.withCollectionPath(collection.getPath())
				.withTimestamp(timestamp)
				.build();
			Logger.logOperation(logEntry);
		} catch (Exception e) {
			LogEntry logEntry = new LogEntry.Builder("[Add FAILED]")
				.withID(data.id)
				.withCollectionPath(collection.getPath())
				.build();
			Logger.logOperation(logEntry);
			System.out.println(e.getMessage());
		}
	}

	public void deleteDocument(CollectionReference collection, String docID) {
		ApiFuture<WriteResult> writeResult = collection.document(docID).delete();
		try {
			Timestamp timestamp = writeResult.get().getUpdateTime();
			LogEntry logEntry = new LogEntry.Builder("[Delete]")
				.withID(docID)
				.withCollectionPath(collection.getPath())
				.withTimestamp(timestamp)
				.build();
			Logger.logOperation(logEntry);
		} catch (Exception e) {
			LogEntry logEntry = new LogEntry.Builder("[Delete FAILED]")
				.withID(docID)
				.withCollectionPath(collection.getPath())
				.build();
			Logger.logOperation(logEntry);
			e.printStackTrace();
		}
	}

	// Returns a null object if there is no object with given document ID.
	public DocumentSnapshot getDocument(String docID, CollectionReference collection) {
		DocumentReference docRef = collection.document(docID);
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = null;
		try {
			// future.get() blocks on response.
			document = future.get();
			LogEntry logEntry = new LogEntry.Builder("[Get]")
				.withID(docID)
				.withCollectionPath(collection.getId())
				.withTimestamp(document.getReadTime())
				.build();
			Logger.logOperation(logEntry);
		} catch (Exception e) {
			LogEntry logEntry = new LogEntry.Builder("[Get FAILED]")
				.withID(docID)
				.withCollectionPath(collection.getId())
				.build();
			Logger.logOperation(logEntry);
			e.printStackTrace();
		}
		return document;
	}

	public <T> List<T> getCollection(CollectionReference collection, Class<T> valueType) {
		List<T> objects = new ArrayList<T>();
		ApiFuture<QuerySnapshot> future = collection.get();
		List<QueryDocumentSnapshot> documents = new ArrayList<QueryDocumentSnapshot>();

		try {
			QuerySnapshot querySnapshot = future.get();
			documents = querySnapshot.getDocuments();
			LogEntry logEntry = new LogEntry.Builder("[Get Collection]")
				.withCollectionPath(collection.getPath())
				.withTimestamp(querySnapshot.getReadTime())
				.build();
			Logger.logOperation(logEntry);
		} catch (Exception e) {
			LogEntry logEntry = new LogEntry.Builder("[Get Collection FAILED]")
				.withCollectionPath(collection.getPath())
				.build();
			Logger.logOperation(logEntry);
			e.printStackTrace();
		}

		for (DocumentSnapshot document : documents) {
			objects.add(document.toObject(valueType));
		}

		return objects;
	}

	public CollectionReference getCollectionReference(String path) {
		return db.collection(path);
	}

	public List<QueryDocumentSnapshot> findDocuments(String field, String value, CollectionReference collection) {
		Query query = collection.whereEqualTo(field, value);
		ApiFuture<QuerySnapshot> future = query.get();
		List<QueryDocumentSnapshot> documents = new ArrayList<QueryDocumentSnapshot>();

		try {
			documents = future.get().getDocuments();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return documents;
	}

	public void setDocument(CollectionReference collection, Data data) {
		DocumentReference docRef = collection.document(data.id);
		ApiFuture<WriteResult> future = docRef.set(data);

		try {
			Timestamp timestamp = future.get().getUpdateTime();
			LogEntry logEntry = new LogEntry.Builder("[Set]")
				.withID(data.id)
				.withCollectionPath(collection.getPath())
				.withTimestamp(timestamp)
				.build();
			Logger.logOperation(logEntry);
		} catch (Exception e) {
			LogEntry logEntry = new LogEntry.Builder("[Set FAILED]")
				.withID(data.id)
				.withCollectionPath(collection.getPath())
				.build();
			Logger.logOperation(logEntry);
			System.out.println(e.getMessage());
		}
	}

	public <T> T toObject(DocumentSnapshot document, Class<T> valueType) {
		T object = null;
		if (document.exists()) {
			object = document.toObject(valueType);
		} else {
			System.out.println("No such document!");
		}
		return object;
	}

	// Tries to set Google Credentials from a file stream. Sets it to null if stream fails.
	public GoogleCredentials getGoogleCredentials(String filePath) {
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
	public FileInputStream getCredentialsStream(String filePath) {
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