package thop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import databaseConnections.MSSQLConnection;
import databaseConnections.MariaDBConnection;
import databaseConnections.MySQLConnection;
import databaseConnections.PostgreSQLConnection;
import databaseConnections.SqlLiteConnection;
import mongoDB.MongoDBConnection;
import threads.ConvertTableThreadMSSQL;
import threads.ConvertTableThreadMariaDB;
import threads.ConvertTableThreadMySQL;
import threads.ConvertTableThreadPostgres;
import threads.ConvertTableThreadSQLite;

public class ConversionApp {
	/**
	 * Funkcija koja na osnovi odabira baze i svih podataka pokreæe odgovarajuæe
	 * metode za konverziju baza
	 * 
	 * @param nazivBaze
	 * @param db_url
	 * @param user
	 * @param pass
	 * @param nazivMongoDBBaze
	 * @param odabraniSQL
	 * @return
	 */
	public boolean startConversionApplication(String nazivBaze, String db_url, String user, String pass,
			String nazivMongoDBBaze, String odabraniSQL) {
		List<String> listaTablica = new ArrayList<String>();
		String start = null, end = null;
		ExecutorService executor = Executors.newFixedThreadPool(5);

		try {
			MongoDBConnection mangodbc = new MongoDBConnection();
			mangodbc.spojiSeNaMongoDBBazu(nazivMongoDBBaze);
			if (odabraniSQL.equals("SQLite")) {
				SqlLiteConnection sqlite = new SqlLiteConnection(db_url);
				start = sqlite.spojiSeNaBazu();
				listaTablica = sqlite.dohvatiSveTabliceUBazi();
				for (String tablica : listaTablica) {
					Runnable worker = new ConvertTableThreadSQLite(mangodbc, sqlite, tablica);
					executor.execute(worker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				end = sqlite.zatvoriVezu();
			} else if (odabraniSQL.equals("Microsoft SQL")) {
				MSSQLConnection mssql = new MSSQLConnection(db_url, nazivBaze, user, pass);
				start = mssql.spojiSeNaBazu();
				listaTablica = mssql.dohvatiSveTabliceUBazi();
				for (String tablica : listaTablica) {
					Runnable worker = new ConvertTableThreadMSSQL(mangodbc, mssql, tablica);
					executor.execute(worker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				end = mssql.zatvoriVezu();
			} else if (odabraniSQL.equals("PostgreSQL")) {
				PostgreSQLConnection pgsql = new PostgreSQLConnection(db_url, user, pass);
				start = pgsql.spojiSeNaBazu();
				listaTablica = pgsql.dohvatiSveTabliceUBazi();
				for (String tablica : listaTablica) {
					Runnable worker = new ConvertTableThreadPostgres(mangodbc, pgsql, tablica);
					executor.execute(worker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				end = pgsql.zatvoriVezu();
			} else if (odabraniSQL.equals("MariaDB")) {
				MariaDBConnection mdbc = new MariaDBConnection(db_url, nazivBaze, user, pass);
				start = mdbc.spojiSeNaBazu();
				listaTablica = mdbc.dohvatiSveTabliceUBazi();
				for (String tablica : listaTablica) {
					Runnable worker = new ConvertTableThreadMariaDB(mangodbc, mdbc, tablica);
					executor.execute(worker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				end = mdbc.zatvoriVezu();
			} else if (odabraniSQL.equals("MySQL")) {
				MySQLConnection msqlc = new MySQLConnection(db_url, nazivBaze, user, pass);
				start = msqlc.spojiSeNaBazu();
				listaTablica = msqlc.dohvatiSveTabliceUBazi();
				for (String tablica : listaTablica) {
					Runnable worker = new ConvertTableThreadMySQL(mangodbc, msqlc, tablica);
					executor.execute(worker);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				end = msqlc.zatvoriVezu();
			}
			mangodbc.zatvoriVezuSaBazom(nazivMongoDBBaze);
			System.out.println("Start: " + start + "\nEnd: " + end);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}