import java.util.ArrayList;
import java.util.List;

public class Director extends Data {
	String name;
	String surname;
	Date birthDate;
	List<Movie> movies;

	public Director() {
		movies = new ArrayList<Movie>();
	}

	public Director(String name, String surname) {
		this();
		this.name = name;
		this.surname = surname;
	}

	public Director(String name, String surname, Date birthDate) {
		this(name, surname);
		this.birthDate = birthDate;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void addMovie(Movie movie) {
		movies.add(movie);
	}

	public void removeMovie(Movie movie) {
		movies.remove(movie);
	}
}

class Date {
	protected int day;
	protected int month;
	protected int year;

	public Date() {
	}

	public Date(int day, int month, int year) {
		this.day = clampDay(day);
		this.month = clampMonth(month);
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	private int clamp(int value, int min, int max) {
		if (value < min) {
			value = min;
		} else if (value > max) {
			value = max;
		}
		return value;
	}

	private int clampDay(int day) {
		return clamp(day, 1, 31);
	}

	private int clampMonth(int month) {
		return clamp(day, 1, 12);
	}

}

class NullDirector extends Director {
	public NullDirector() {
		super("TBA", "TBA", new NullDate());
	}

	@Override
	public void addMovie(Movie movie) {
	}

	@Override
	public void removeMovie(Movie movie) {
	}
}

class NullDate extends Date{
	public NullDate(){
		day = 0;
		month = 0;
		year = 0;
	}
}
