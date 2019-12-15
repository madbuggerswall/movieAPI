import com.google.gson.Gson;

import static spark.Spark.*;

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
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
			response.header("Access-Control-Allow-Credentials", "true");
			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
			response.header("Access-Control-Allow-Credentials", "true");
		});
	}

	void handlePaths() {
		setResponseHeaders();
		before("/*", (q, a) -> System.out.println("Received API call"));
		path("/movies", () -> {
			get("", (request, response) -> {
				response.type("application/json");
				return gson.toJson(database.getAllMovies());
			});
			get("/:id", (request, response) -> {
				response.type("application/json");
				return database.getMovie(request.params(":id")).toJSON();
			});
			post("", (request, response) -> {
				database.addMovie(gson.fromJson(request.body(), Movie.class));
				return null;
			});
			put("/:id", (request, response) -> {
				database.setMovie(gson.fromJson(request.body(), Movie.class));
				return response;
			});
		});
		path("/directors", () -> {
			get("", (request, response) -> {
				response.type("application/json");
				return gson.toJson(database.getAllDirectors());
			});
			get("/:id", (request, response) -> {
				response.type("application/json");
				return database.getDirector(request.params(":id")).toJSON();
			});
			post("", (request, response) -> {
				database.addDirector(gson.fromJson(request.body(), Director.class));
				return response;
			});
			put("/:id", (request, response) -> {
				database.setDirector(gson.fromJson(request.body(), Director.class));
				return response;
			});
		});
	}
}
