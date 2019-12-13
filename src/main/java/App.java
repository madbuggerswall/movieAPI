import static spark.Spark.*;

public class App {
	public static void main(String[] args) {
		System.out.println("Main");
		
	}
}

class RequestHandler {
	int port = 8080;
	Database database;

	public RequestHandler() {
		database = Database.getInstance();
		port(port);
		handleGetRequests();
	}

	void handleGetRequests() {
		get("/movies", (request, response) -> "getAllMovies");
		get("/movies/:id", (request, response) -> {
			return database.getMovie(request.params(":id"));
		});
	}
}
