import java.io.FileNotFoundException;

public class AppTest {
	public static void main(String[] args) throws FileNotFoundException {
		
	}

	// Don't call unless database is empty.
	static void addTopTenToDB() {
		DatabaseManager dbManager = DatabaseManager.getInstance();
		Director darabont = new Director("Frank", "Darabont", new Date(28, 1, 1959));
		Director coppola = new Director("Francis Ford", "Coppola", new Date(7, 4, 1939));
		Director nolan = new Director("Christopher", "Nolan", new Date(30, 7, 1970));
		Director lumet = new Director("Sidney", "Lumet", new Date(25, 6, 1924));
		Director spielberg = new Director("Steven", "Spielberg", new Date(18, 12, 1946));
		Director peterJackson = new Director("Peter", "Jackson", new Date(31, 10, 1961));
		Director tarantino = new Director("Quentin", "Tarantino", new Date(27, 3, 1963));
		Director sergioLeone = new Director("Sergio", "Leone", new Date(3, 1, 1929));
		Director davidFincher = new Director("David", "Fincher", new Date(28, 8, 1962));

		dbManager.addDirector(darabont);
		dbManager.addDirector(coppola);
		dbManager.addDirector(nolan);
		dbManager.addDirector(lumet);
		dbManager.addDirector(spielberg);
		dbManager.addDirector(peterJackson);
		dbManager.addDirector(tarantino);
		dbManager.addDirector(sergioLeone);
		dbManager.addDirector(davidFincher);

		Movie shawshank = new Movie("Shawshank Redemption", 1994, 142, darabont);
		Movie godfather = new Movie("The Godfather", 1972, 175, coppola);
		Movie godfatherII = new Movie("The Godfather: Part II", 1974, 212, coppola);
		Movie darkKnight = new Movie("The Dark Knight", 2008, 152, nolan);
		Movie angryMen = new Movie("12 Angry Men", 1957, 96, lumet);
		Movie schindler = new Movie("Schindler's List", 1993, 195, spielberg);
		Movie lotr3 = new Movie("The Lord of the Rings: The Return of the King", 2003, 201, peterJackson);
		Movie pulp = new Movie("Pulp Fiction", 1994, 154, tarantino);
		Movie goodBadUgly = new Movie("The Good, the Bad and the Ugly", 1966, 148, sergioLeone);
		Movie fightClub = new Movie("The Fight Club", 1999, 139, davidFincher);

		dbManager.addMovie(shawshank);
		dbManager.addMovie(godfather);
		dbManager.addMovie(godfatherII);
		dbManager.addMovie(darkKnight);
		dbManager.addMovie(angryMen);
		dbManager.addMovie(schindler);
		dbManager.addMovie(lotr3);
		dbManager.addMovie(pulp);
		dbManager.addMovie(goodBadUgly);
		dbManager.addMovie(fightClub);
	}

}