public class AppTest {
	static DatabaseManager databaseManager;

	public static void main(String[] args) {
		System.out.println("Test begins...");
		databaseManager = new DatabaseManager("credentials.json");
		findMovies("The Godfathe");
	}

	static void addReadMovie() {
		Movie shawshank = new Movie("Shawshank Redemption", 1994, 144);
		databaseManager.addMovie(shawshank);

		Movie movie = databaseManager.getMovie("Shawshank Redemption");
		System.out.println(movie.title);
		System.out.println(movie.releaseYear);
		System.out.println(movie.duration);
	}

	static void addUser() {
		User user = new User("madbuggerswall");
		user.name = "Furkan";
		user.surname = "Bilgin";

		Movie shawshank = databaseManager.getMovie("Shawshank Redemption");
		Movie pulpFiction = databaseManager.getMovie("Pulp Fiction");
		Movie matrix = databaseManager.getMovie("The Matrix");
		Movie godfather = databaseManager.getMovie("The Godfather");

		MovieList favoriteMovies = new MovieList("My favorite movies");
		favoriteMovies.addMovie(shawshank);
		favoriteMovies.addMovie(pulpFiction);
		favoriteMovies.addMovie(matrix);

		MovieList watchList = new MovieList("Watch List");
		watchList.addMovie(godfather);

		user.addList(favoriteMovies);
		user.addList(watchList);
		databaseManager.addUser(user);
	}

	static void findMovies(String query){
		for (Movie movie : databaseManager.findMovies(query)) {
			System.out.println(movie.title);
		}
	}
}