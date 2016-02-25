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
 * Klasa koja sadrži sve metode za spajanje i dohvaæanje podataka sa Microsoft
 * SQL baze
 * 
 * @author worx-pc-01
 *
 */
public class MSSQLConnection {
	// Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	private String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private String DB_URL = "jdbc:sqlserver://localhost;databaseName=world;integratedSecurity=true;";
	// Database credentials
	// private String USER = "";
	// private String PASS = "";

	// Connection
	private Connection conn = null;

	/**
	 * Konstruktor sa svim podacima za Microsoft SQL bazu
	 * 
	 * @param DB_URL
	 * @param nazivBaze
	 * @param USER
	 * @param PASS
	 */
	public MSSQLConnection(String DB_URL, String nazivBaze, String USER, String PASS) {
		super();
		this.DB_URL = DB_URL;
		// this.USER = USER;
		// this.PASS = PASS;

	}

	/**
	 * Spajanje na bazu
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String spojiSeNaBazu() throws ClassNotFoundException, SQLException {
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL);
		return timeStamp;
	}

	/**
	 * Zatvaranje baze sa bazom
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
		ResultSet rs = null;
		Statement stmt = (Statement) conn.createStatement();
		// String query = "SELECT Distinct TABLE_NAME FROM
		// information_schema.TABLES;";
		String query = "SELECT Distinct TABLE_SCHEMA+'.'+TABLE_NAME as tablename FROM information_schema.TABLES;";
		rs = (ResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String tableName = rs.getString("tablename");
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
		String query = "exec sp_columns " + sredi(nazivTablice) + ";";
		rs = (ResultSet) stmt.executeQuery(query);
		while (rs.next()) {
			String field = rs.getString("COLUMN_NAME");
			String type = rs.getString("TYPE_NAME");
			Boolean canBeNull = isNullable(rs.getString("NULLABLE"));
			String defaultValue = rs.getString("COLUMN_DEF");
			String extra = rs.getString("TABLE_NAME");
			listaNaziviStupacaUTablici
					.add(new TipStupcaTablice(field, type, canBeNull, KeyType.Random, defaultValue, extra));
		}
		return listaNaziviStupacaUTablici;
	}

	/**
	 * Micanje toèke iz naziva tablice jer inaèe ne rade naredbe u MongoDB
	 * Shellu
	 * 
	 * @param s
	 * @return
	 */
	public String sredi(String s) {
		String[] data = s.split("\\.");
		return data[1];
	}

	/**
	 * Funkcija koja na osnovi string koji dobije vraæa true ili false ako
	 * vrijednost može biti null
	 * 
	 * @param nullable
	 * @return
	 */
	private Boolean isNullable(String nullable) {
		if (nullable.equals("1")) {
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
		String query = "SELECT * FROM " + nazivTablice + ";";
		
		// View koji ne valja u bazi i ignoriranje viewova kako bi ubrzali
		// aplikaciju
		if (query.equals("SELECT * FROM dpdm.PB_USER_PROGRAM_JOB;") || query.contains("view")) {
			listaRedakaUTablici = null;
			return listaRedakaUTablici;
		}
		rs = (ResultSet) stmt.executeQuery(query);
		ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
		int brojStupaca = rsmd.getColumnCount();
		int help = 0;
		while (rs.next()) {
			help++;
			List<String> redak = new ArrayList<String>();
			for (int i = 1; i <= brojStupaca; i++) {
				String columnValue = rs.getString(i);
				redak.add(columnValue);
			}
			listaRedakaUTablici.add(redak);

			// hvatanje samo 200 000 redaka iz tablice
			if (help == 200000)
				break;
		}
		return listaRedakaUTablici;
	}
}
