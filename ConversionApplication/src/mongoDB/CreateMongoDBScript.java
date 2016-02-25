package mongoDB;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Klasa koja sadrži funkciju za spremanje txt datoteka
 * 
 * @author worx-pc-01
 *
 */
public class CreateMongoDBScript {
	/**
	 * Funkcija koja sprema txt file na data/ folder
	 * 
	 * @param skripta
	 * @param nazivDatoteke
	 */
	public void spremiSkriptu(String skripta, String nazivDatoteke) {
		PrintWriter out = null;
		try {
			out = new PrintWriter("data/" + nazivDatoteke + ".txt");
			out.println(skripta);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.close();
	}
}
