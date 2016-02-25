package databaseConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.postgresql.jdbc.PgResultSet;
import org.postgresql.jdbc.PgResultSetMetaData;
import org.postgresql.jdbc.PgStatement;

import data.Index;
import data.TipStupcaTablice;
import data.TipStupcaTablice.KeyType;

/**
 * Klasa koja sadrži sve metode za spajanje i dohvaæanje podataka sa MySQL baze
 * 
 * @author worx-pc-01
 *
 */
public class PostgreSQLConnection {
	// Database credentials
	private String DB_URL = "jdbc:postgresql://localhost:3308/world";
	private String USER = "postgres";
	private String PASS = "lozinka";

	// Connection
	private Connection conn = null;

	/**
	 * Konstruktor sa svim podacima za Postgres bazu
	 * 
	 * @param dB_URL
	 * @param nazivBaze
	 * @param uSER
	 * @param pASS
	 */
	public PostgreSQLConnection(String DB_URL, String USER, String PASS) {
		super();
		this.USER = USER;
		this.PASS = PASS;
		this.DB_URL = DB_URL;
	}

	/**
	 * Spajanje na bazu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String spojiSeNaBazu() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		return timeStamp;
	}

	/**
	 * Zatvaranje veze sa bazom
	 * 
	 * @throws SQLException
	 */
	public String zatvoriVezu() throws SQLException {
		conn.close();
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		return timeStamp;
	}

	/**
	 * Funkcija koja dohvaæa nazive svih tablica koje nisu default u bazi
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> dohvatiSveTabliceUBazi() throws SQLException {
		List<String> listaNaziviTablica = new ArrayList<String>();
		PgResultSet rs = null;
		PgStatement stmt = (PgStatement) conn.createStatement();
		String query = "SELECT table_name FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema NOT IN ('pg_catalog', 'information_schema');";
		rs = (PgResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String tableName = rs.getString("table_name");
			listaNaziviTablica.add(tableName);
		}
		return listaNaziviTablica;
	}

	/**
	 * Tablica koja dohvaæa sve stupce u tablici te njihove tipove i sve podatke
	 * o stupcu
	 * 
	 * @param nazivTablice
	 * @return
	 * @throws SQLException
	 */
	public List<TipStupcaTablice> dohvatiNaziveStupacaUTablici(String nazivTablice) throws SQLException {
		List<TipStupcaTablice> listaNaziviStupacaUTablici = new ArrayList<TipStupcaTablice>();
		PgResultSet rs = null;
		PgStatement stmt = (PgStatement) conn.createStatement();
		String query = "select column_name, data_type, is_nullable, column_default from INFORMATION_SCHEMA.COLUMNS where table_name = '"
				+ nazivTablice + "';";
		rs = (PgResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String field = rs.getString("column_name");
			String type = rs.getString("data_type");
			Boolean canBeNull = isNullable(rs.getString("is_nullable"));
			String defaultValue = rs.getString("column_default");
			String extra = "postgres";
			listaNaziviStupacaUTablici
					.add(new TipStupcaTablice(field, type, canBeNull, KeyType.Random, defaultValue, extra));
		}
		return listaNaziviStupacaUTablici;
	}

	/**
	 * Funkcija koja na osnovi string koji dobije vraæa true ili false ako
	 * vrijednost može biti null
	 * 
	 * @param nullable
	 * @return
	 */
	private Boolean isNullable(String nullable) {
		if (nullable.equals("YES")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Funkcija koja gleda koji je tip kljuèa i odabire odgovarajuæi enum
	 * 
	 * @param tip
	 * @return
	 */
	/*
	 * private KeyType tipKljuca(String tip) { // TODO Otkriti kako znati ako je
	 * primary ili nekakvi drukciji key switch (tip) { case "PRI": return
	 * KeyType.Primary; case "FOR": return KeyType.Foreign; default: return
	 * KeyType.Random; } }
	 */

	/**
	 * Funkcija koja dohvaæa sve podatke iz tablice
	 * 
	 * @param nazivTablice
	 * @return
	 * @throws SQLException
	 */
	public List<List<String>> dohvatiSveRetkeUTablici(String nazivTablice) throws SQLException {
		List<List<String>> listaRedakaUTablici = new ArrayList<List<String>>();
		PgResultSet rs = null;
		PgStatement stmt = (PgStatement) conn.createStatement();
		String query = "SELECT * FROM " + nazivTablice + ";";
		rs = (PgResultSet) stmt.executeQuery(query);
		PgResultSetMetaData rsmd = (PgResultSetMetaData) rs.getMetaData();
		int brojStupaca = rsmd.getColumnCount();
		while (rs.next()) {
			List<String> redak = new ArrayList<String>();
			for (int i = 1; i <= brojStupaca; i++) {
				String columnValue = rs.getString(i);
				redak.add(columnValue);
			}
			listaRedakaUTablici.add(redak);
		}
		return listaRedakaUTablici;
	}

	/**
	 * Funkcija koja vraæa listu svih indeksa sa svim stupcima koje baza vraæa
	 * 
	 * @param nazivTablice
	 * @return
	 * @throws SQLException
	 */
	public List<Index> dohvatiSveIndekseUTablici(String nazivTablice) throws SQLException {
		List<Index> listaRedakaUIndexima = new ArrayList<Index>();

		// TODO Dohvati indekse

		return listaRedakaUIndexima;
	}
}
