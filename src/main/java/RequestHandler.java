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
			response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
			response.header("Access-Control-Allow-Credentials", "true");
			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
			response.header("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
			response.header("Access-Control-Allow-Credentials", "true");
		});
	}

	void handlePaths() {
		setResponseHeaders();
		before("/*", (q, a) -> System.out.println("Received API call"));

		// Movie
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
				return response.status();
			});

			// Update movie.
			put("/:id", (request, response) -> {
				database.setMovie(gson.fromJson(request.body(), Movie.class));
				response.status(200);
				return response.status();
			});

			// Delete movie.
			delete("/:id", (request, response) -> {
				database.deleteMovie(request.params(":id"));
				response.status(200);
				return response.status();
			});
		});

		// Director
		path("/directors", () -> {

			// Get All Directors
			get("", (request, response) -> {
				response.type("application/json");
				return gson.toJson(database.getAllDirectors());
			});

			// Get Director w/id
			get("/:id", (request, response) -> {
				response.type("application/json");
				return database.getDirector(request.params(":id")).toJSON();
			});

			// Add new Director
			post("", (request, response) -> {
				database.addDirector(gson.fromJson(request.body(), Director.class));
				response.status(200);
				return response.status();
			});

			// Update Director
			put("/:id", (request, response) -> {
				database.setDirector(gson.fromJson(request.body(), DirectorDTO.class));
				response.status(200);
				return response.status();
			});

			// Delete Director
			delete("/:id", (request, response) -> {
				database.deleteDirector(request.params(":id"));
				response.status(200);
				return response.status();
			});
		});

		// User
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
				String userID = request.params(":userID");
				int listIndex = Integer.parseInt(request.params(":listIndex"));
				String movieID = request.params(":movieID");
				response.type("application/json");
				database.addMovieToUserList(userID, listIndex, movieID);
				response.status(200);
				return response.status();
			});

			// Add new user.
			post("", (request, response) -> {
				User user = gson.fromJson(request.body(), User.class);
				if (database.userNameExists(user.username)) {
					response.status(404);
					response.body("Username already exists.");
					return response.status();
				} else {
					database.addUser(user);
					response.status(200);
					return response.status();
				}
			});

			// Login user.
			post("/login", (request, response) -> {
				UserDTO userDTO = gson.fromJson(request.body(), UserDTO.class);
				if (database.userNameExists(userDTO.username)) {
					User user = database.loginUser(userDTO);
					if (user != null) {
						response.status(200);
						response.type("application/json");
						return gson.toJson(new LoginResponse(response.status(), user));
					} else {
						response.status(404);
						response.body("Incorrect password");
						return response.status();
					}
				} else {
					response.status(404);
					response.body("No such user with username: " + userDTO.username);
					return response.status();
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
					return response.status();
				}
			});

			// Update user.
			put("/:id", (request, response) -> {
				database.setUser(gson.fromJson(request.body(), User.class));
				response.status(200);
				return response.status();
			});

			// Delete user.
			delete("/:id", (request, response) -> {
				database.deleteUser(request.params(":id"));
				response.status(200);
				return response.status();
			});
		});
	}
}
