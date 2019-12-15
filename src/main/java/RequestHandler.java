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
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
			response.header("Access-Control-Allow-Credentials", "true");
			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
			response.header("Access-Control-Allow-Credentials", "true");
		});
	}

	void handlePaths() {
		setResponseHeaders();
		before("/*", (q, a) -> System.out.println("Received API call"));

		path("/movies", () -> {

			// Get all movies.
			get("", (request, response) -> {
				response.type("application/json");
				response.status(200);
				return gson.toJson(database.getAllMovies());
			});

			// Get movie w/id.
			get("/:id", (request, response) -> {
				response.type("application/json");
				return database.getMovie(request.params(":id")).toJSON();
			});

			// Add new movie.
			post("", (request, response) -> {
				database.addMovie(gson.fromJson(request.body(), Movie.class));
				response.status(200);
				return response;
			});

			// Update movie.
			put("/:id", (request, response) -> {
				database.setMovie(gson.fromJson(request.body(), Movie.class));
				response.status(200);
				return response;
			});

			// Delete movie.
			delete("/:id", (request, response) -> {
				System.out.println("delete");
				database.deleteMovie(request.params(":id"));
				response.status(200);
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
			delete("/:id", (request, response) -> {
				database.deleteDirector(request.params(":id"));
				return response;
			});
		});

		path("/users", () -> {
			// Get all users.
			get("", (request, response) -> {
				response.type("application/json");
				return gson.toJson(database.getAllUsers());
			});

			// Get user w/id
			get("/:id", (request, response) -> {
				response.type("application/json");
				return database.getUser(request.params(":id")).toJSON();
			});

			// Add movie to user list.
			get("/:userID/:listIndex/:movieID", (request, response) -> {
				String userID = request.params("userID");
				int listIndex = Integer.parseInt(request.params("userID"));
				String movieID = request.params("userID");
				response.type("application/json");
				database.addMovieToUserList(userID, listIndex, movieID);
				response.status(200);
				return response;
			});

			// Add new user.
			post("", (request, response) -> {
				User user = gson.fromJson(request.body(), User.class);
				if (database.userNameExists(user.username)) {
					response.status(404);
					response.body("Username already exists.");
					return response.body();
				} else {
					database.addUser(user);
					response.status(200);
					return response.body();
				}
			});

			// Login user.
			post("/login", (request, response) -> {
				UserDTO userDTO = gson.fromJson(request.body(), UserDTO.class);
				if (database.userNameExists(userDTO.username)) {
					User user = database.loginUser(userDTO);
					if (user != null) {
						response.status(200);
						return gson.toJson(user);
					} else {
						response.status(404);
						response.body("Incorrect password");
						return response.body();
					}
				} else {
					response.status(404);
					response.body("No such user with username: " + userDTO.username);
					return response.body();
				}
			});

			get("/hasLoggedIn/:id", (request, response) -> {
				User user = database.hasLoggedIn(request.params(":id"), request.body());
				if (user != null) {
					response.status(200);
					return gson.toJson(user);
				} else {
					response.status(404);
					response.body("User is not logged in.");
					return response.body();
				}
			});

			// Update user.
			put("/:id", (request, response) -> {
				database.setUser(gson.fromJson(request.body(), User.class));
				return response;
			});

			// Delete user.
			delete("/:id", (request, response) -> {
				database.deleteUser(request.params(":id"));
				return response;
			});
		});
	}
}
