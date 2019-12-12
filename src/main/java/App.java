import static spark.Spark.*;

public class App {
	public static void main(String[] args) {
		System.out.println("Main");
		
	}
}

class RequestHandler {
	int port = 8080;

	public RequestHandler() {
		System.out.println("Request handler.");
		port(port);
		handleGetRequests();
	}

	void handleGetRequests(){
		get("/movies", (request, response) -> "getAllMovies");
		get("/movies/:id", (request, response) -> {
			return "getMovie: " + request.params(":id");
		});
	}
}
