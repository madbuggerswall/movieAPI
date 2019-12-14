import static spark.Spark.*;
import com.google.gson.Gson;

class RequestHandler {
	int port = 8888;
	private Database database;
	private Gson gson = new Gson();

	public RequestHandler() {
		database = Database.getInstance();
		port(port);
	}

	void setResponseHeaders() {
		options("/*", (request, response) -> {
			String accessControlRequestHeaders = request
				.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers",
					accessControlRequestHeaders);
			}
			String accessControlRequestMethod = request
				.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods",
					accessControlRequestMethod);
			}

			return "OK";
		});
		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.type("application/json");
		});
	}

	void handlePaths() {
		setResponseHeaders();
		path("/api", () -> {
			before("/*", (q, a) -> System.out.println("Received api call"));
			path("/movie", () -> {
				get("/get/id/:id", (request, response) -> {
					return database.getMovie(request.params(":id")).toJSON();
				});
				get("/get/all", (request, response) -> {
					response.type("application/json");
					return gson.toJson(database.getAllMovies());
				});
				post("/add", (request, response) -> {
					database.addMovie(gson.fromJson(request.body(), Movie.class));
					return null;
				});
				post("/set", (request, response) -> {
					database.setMovie(gson.fromJson(request.body(), Movie.class));
					return null;
				});
			});
			path("/director", () -> {
				get("/get/id/:id", (request, response) -> database.getDirector(request.params(":id")).toJSON());
				get("/get/all", (request, response) -> gson.toJson(database.getAllDirectors()));
				post("/add", (request, response) -> {
					database.addDirector(gson.fromJson(request.body(), Director.class));
					return null;
				});
				post("/set", (request, response) -> {
					database.setDirector(gson.fromJson(request.body(), Director.class));
					return null;
				});
			});
		});
	}
}
