import java.io.*;

import com.google.cloud.Timestamp;

public class Logger {
	private static String fileName = "log.txt";
	private static PrintWriter writer;

	private static void log(String message) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(new File(fileName), true);
			writer = new PrintWriter(fileOutput);
		} catch (Exception e) {
			System.out.println("Unable to open file '" + fileName + "'");
			e.printStackTrace();
		}
		writer.append(message);
		writer.flush();
	}

	public static void logOperation(LogEntry logEntry) {
		String logMessage = logEntry.operation + "\t";
		if (logEntry.id != null) {
			logMessage += "docID: " + logEntry.id + "\t";
		}
		if (logEntry.collectionPath != null) {
			logMessage += "Collection: " + logEntry.collectionPath + "\t";
		}
		if (logEntry.timestamp != null) {
			logMessage += " Time: " + logEntry.timestamp;
		}
		logMessage += "\n";
		Logger.log(logMessage);
	}
}

class LogEntry {
	String operation;
	String id;
	String collectionPath;
	Timestamp timestamp;

	public LogEntry() {
	}

	public static class Builder {
		String operation;
		String id;
		String collectionPath;
		Timestamp timestamp;

		public Builder(String operation) {
			this.operation = operation;
		}

		public Builder withID(String id) {
			this.id = id;
			return this;
		}

		public Builder withCollectionPath(String collectionPath) {
			this.collectionPath = collectionPath;
			return this;
		}

		public Builder withTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public LogEntry build() {
			LogEntry logEntry = new LogEntry();
			logEntry.operation = this.operation;
			logEntry.id = this.id;
			logEntry.collectionPath = this.collectionPath;
			logEntry.timestamp = this.timestamp;
			return logEntry;
		}
	}
}
