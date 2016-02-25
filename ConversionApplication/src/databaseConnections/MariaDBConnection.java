package databaseConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mariadb.jdbc.MariaDbResultSet;
import org.mariadb.jdbc.MariaDbResultSetMetaData;
import org.mariadb.jdbc.MariaDbStatement;

import data.Index;
import data.TipStupcaTablice;
import data.TipStupcaTablice.KeyType;

/**
 * Klasa koja sadrži sve metode za spajanje i dohvaæanje podataka sa MariaDB
 * baze
 * 
 * @author worx-pc-01
 *
 */
public class MariaDBConnection {
	// Database credentials
	private String DB_URL = "jdbc:mariadb://127.0.0.1:3307/world";
	private String nazivBaze;
	private String USER = "root";
	private String PASS = "lozinka";

	// Connection
	private Connection conn = null;

	/**
	 * Konstruktor sa svim podacima za MariaDB bazu
	 * 
	 * @param dB_URL
	 * @param nazivBaze
	 * @param uSER
	 * @param pASS
	 */
	public MariaDBConnection(String DB_URL, String nazivBaze, String USER, String PASS) {
		super();
		this.nazivBaze = nazivBaze;
		this.USER = USER;
		this.PASS = PASS;
		this.DB_URL = DB_URL;
	}

	/**
	 * Spajanje na bazu
	 * 
	 * @throws SQLException
	 */
	public String spojiSeNaBazu() throws SQLException {
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
	 * Funkcija koja dohvaæa nazive svih tablica u bazi
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<String> dohvatiSveTabliceUBazi() throws SQLException {
		List<String> listaNaziviTablica = new ArrayList<String>();
		MariaDbResultSet rs = null;
		MariaDbStatement stmt = (MariaDbStatement) conn.createStatement();
		// Statement stmt = (Statement) conn.createStatement();
		String query = "SHOW TABLES;";
		rs = (MariaDbResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String tableName = rs.getString("Tables_in_" + nazivBaze);
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
		MariaDbResultSet rs = null;
		MariaDbStatement stmt = (MariaDbStatement) conn.createStatement();
		String query = "DESCRIBE " + nazivTablice + ";";
		rs = (MariaDbResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String field = rs.getString("Field");
			String type = rs.getString("Type");
			Boolean canBeNull = rs.getBoolean("Null");
			String defaultValue = rs.getString("Default");
			String extra = rs.getString("Extra");
			listaNaziviStupacaUTablici
					.add(new TipStupcaTablice(field, type, canBeNull, tipKljuca(type), defaultValue, extra));
		}
		return listaNaziviStupacaUTablici;
	}

	/**
	 * Funkcija koja gleda tip kljuèa i odabire odgovarajuæi enum
	 * 
	 * @param tip
	 * @return
	 */
	private KeyType tipKljuca(String tip) {
		switch (tip) {
		case "PRI":
			return KeyType.Primary;
		case "FOR":
			return KeyType.Foreign;
		default:
			return KeyType.Random;
		}
	}

	/**
	 * Funkcija koja dohvaæa sve podatke iz tablice
	 * 
	 * @param nazivTablice
	 * @return
	 * @throws SQLException
	 */
	public List<List<String>> dohvatiSveRetkeUTablici(String nazivTablice) throws SQLException {
		List<List<String>> listaRedakaUTablici = new ArrayList<List<String>>();
		MariaDbResultSet rs = null;
		MariaDbStatement stmt = (MariaDbStatement) conn.createStatement();
		String query = "SELECT * FROM " + nazivTablice;
		rs = (MariaDbResultSet) stmt.executeQuery(query);
		MariaDbResultSetMetaData rsmd = (MariaDbResultSetMetaData) rs.getMetaData();
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
	 * Funkcija koja vraæa listu svih indeksa sa svim stupcima koje MariaDB
	 * vraæa
	 * 
	 * @param nazivTablice
	 * @return
	 * @throws SQLException
	 */
	public List<Index> dohvatiSveIndekseUTablici(String nazivTablice) throws SQLException {
		List<Index> listaRedakaUIndexima = new ArrayList<Index>();
		MariaDbResultSet rs = null;
		MariaDbStatement stmt = (MariaDbStatement) conn.createStatement();
		String query = "SHOW INDEX FROM " + nazivTablice;
		rs = (MariaDbResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			listaRedakaUIndexima.add(new Index(rs.getString("Table"), rs.getString("Non_unique"),
					rs.getString("Key_name"), rs.getString("Seq_in_index"), rs.getString("Column_name"),
					rs.getString("Collation"), rs.getString("Cardinality"), rs.getString("Sub_part"),
					rs.getString("Packed"), rs.getString("Null"), rs.getString("Index_type"), rs.getString("Comment"),
					rs.getString("Index_comment")));
		}
		return listaRedakaUIndexima;
	}
}
