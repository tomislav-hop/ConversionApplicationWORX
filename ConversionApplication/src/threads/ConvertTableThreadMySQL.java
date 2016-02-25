package threads;

import java.sql.SQLException;
import java.util.List;

import data.TipStupcaTablice;
import databaseConnections.MySQLConnection;
import mongoDB.MongoDBConnection;

public class ConvertTableThreadMySQL implements Runnable {

	private MongoDBConnection mangodbc;
	private MySQLConnection veza;
	private String tablica;

	public ConvertTableThreadMySQL(MongoDBConnection mangodbc, MySQLConnection veza, String tablica) {
		super();
		this.mangodbc = mangodbc;
		this.veza = veza;
		this.tablica = tablica;
	}

	@Override
	public void run() {
		try {
			// kreiraj tablicu u MongoDB
			mangodbc.kreirajKolekciju(tablica);
			// dohvati stupce u tablici
			List<TipStupcaTablice> listaStupacaTablice = veza.dohvatiNaziveStupacaUTablici(tablica);
			// dohvati redove u tablici
			List<List<String>> listaRedakaTablice = veza.dohvatiSveRetkeUTablici(tablica);
			// dodaj redove u MangoDB
			mangodbc.dodajDokument(tablica, listaStupacaTablice, listaRedakaTablice);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
