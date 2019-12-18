import java.util.ArrayList;
import java.util.List;

public class User extends Data {
	String username;
	String password;
	String role;
	String accessToken;
	String name;
	String surname;

	List<MovieList> lists;

	public User() {
		lists = new ArrayList<MovieList>();
		MovieList watched = new MovieList("Watched");
		MovieList favorites = new MovieList("Favorites");
		addList(watched);
		addList(favorites);
		role = "User";
	}

	public User(String username) {
		this();
		this.username = username;
	}

	public User(String username, String password) {
		this(username);
		this.password = password;
	}

	public User(String username, String password, String name, String surname) {
		this(username, password);
		this.name = name;
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public MovieList getList(int index) {
		return lists.get(index);
	}

	public List<MovieList> getLists() {
		return lists;
	}

	public void addList(MovieList movieList) {
		lists.add(movieList);
	}

	public void removeList(MovieList movieList) {
		lists.remove(movieList);
	}
}

// Credentials
class UserDTO {
	String username;
	String password;

	public UserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
}