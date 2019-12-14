import java.io.*;

import com.google.cloud.Timestamp;

public class Logger {
	private static String fileName = "log.txt";
	private static PrintWriter writer;

	public Logger() {

		try {
			FileOutputStream fileOutput = new FileOutputStream(new File(fileName), true);
			writer = new PrintWriter(fileOutput);
		} catch (Exception e) {
			System.out.println("Unable to open file '" + fileName + "'");
			e.printStackTrace();
		}

	}

	private static void log(String message) {
		writer.append(message);
		writer.flush();
	}

	public static void logOperation(String operation, String id, String collectionPath, Timestamp timestamp) {
		String logMessage = operation + " ";
		logMessage += "docID: " + id + " ";
		logMessage += "Collection: " + collectionPath + " ";
		logMessage += " " + timestamp;
		Logger.log(logMessage);
	}

	public static void logOperation(String operation, String id, String collectionPath) {
		String logMessage = operation;
		logMessage += "docID: " + id + " ";
		logMessage += "Collection: " + collectionPath;
		Logger.log(logMessage);
	}
}

class LogEntry{
	String operation;
	String id;
	String collectionPath;
	Timestamp timestamp;

	public LogEntry(){

	}
}