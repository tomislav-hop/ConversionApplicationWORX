package databaseConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import data.TipStupcaTablice;
import data.TipStupcaTablice.KeyType;

/**
 * Klasa koja sadrži sve metode za spajanje i dohvaæanje podataka sa SQLite baze
 * 
 * @author worx-pc-01
 *
 */
public class SqlLiteConnection {
	private String JDBC_DRIVER = "org.sqlite.JDBC";
	private String DB_URL = "jdbc:sqlite:E:/SQLite/test.db";

	Connection conn = null;

	/**
	 * Konstruktor sa svim podacima za SQLite bazu
	 * 
	 * @param DB_URL
	 */
	public SqlLiteConnection(String DB_URL) {
		super();
		this.DB_URL = DB_URL;
	}

	/**
	 * Spajanje na bazu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String spojiSeNaBazu() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL);
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
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Name FROM sqlite_master WHERE type='table';");
		while (rs.next()) {
			String tableName = rs.getString("Name");
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
		ResultSet rs = null;
		Statement stmt = (Statement) conn.createStatement();
		String query = "PRAGMA table_info([" + nazivTablice + "]);";
		rs = (ResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String field = rs.getString("name");
			String type = rs.getString("type");
			Boolean canBeNull = isNullable(rs.getString("notnull"));
			String defaultValue = rs.getString("dflt_value");
			String extra = rs.getString("pk");
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
		if (nullable.equals("0")) {
			return true;
		} else {
			return false;
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
		ResultSet rs = null;
		Statement stmt = (Statement) conn.createStatement();
		String query = "SELECT * FROM " + nazivTablice;
		rs = (ResultSet) stmt.executeQuery(query);
		ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
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

}
