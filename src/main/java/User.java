import java.util.ArrayList;
import java.util.List;

public class User extends Data {
	String username;
	String password;
	String name;
	String surname;

	List<MovieList> lists;

	public User() {
		lists = new ArrayList<MovieList>();
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

	public String getName() {
		return name;
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

class UserDTO {
	String username;
	String password;
}