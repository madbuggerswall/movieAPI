import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

public class App {
	public static void main(String[] args) {
		System.out.println("Main");
		
	}
}

class RequestHandler {
	int port = 8080;
	DatabaseManager databaseManager;

	public RequestHandler() {
		databaseManager = DatabaseManager.getInstance();
		port(port);
		handleGetRequests();
	}

	void handleGetRequests() {
		get("/movies", (request, response) -> "getAllMovies");
		get("/movies/:id", (request, response) -> {
			return databaseManager.getMovie(request.params(":id"));
		});
	}
}
