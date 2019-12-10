import java.util.ArrayList;
import java.util.List;

public class User {
	String username;
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

	public User(String username, String name, String surname) {
		this(username);
		this.name = name;
		this.surname = surname;
	}

	public String getUsername(){
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

	public void removeList(MovieList movieList){
		lists.remove(movieList);
	}
}