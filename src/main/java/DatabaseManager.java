import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
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
		System.out.println(data.id);
		try {
			System.out.println("Update time : " + future.get().getUpdateTime());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// future.get() blocks on response.
	public DocumentSnapshot getDocument(String docID, CollectionReference collection) {
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

	public <T> List<T> getCollection(CollectionReference collection, Class<T> valueType) {
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

	public CollectionReference getCollectionReference(String path) {
		return db.collection(path);
	}

	public void setDocument(CollectionReference collection, Data data) {
		DocumentReference docRef = collection.document(data.id);
		ApiFuture<WriteResult> future = docRef.set(data);

		try {
			System.out.println("Update time : " + future.get().getUpdateTime());
		} catch (Exception e) {
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