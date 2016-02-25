package mongoDB;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import data.TipStupcaTablice;

/**
 * Klasa koja ima funkcije koje se bave MongoDB bazom (kreiranje, spremanje,
 * èitanje)
 * 
 * @author worx-pc-01
 *
 */
public class MongoDBConnection {

	MongoClient mongoClient;
	MongoDatabase mongoDB;
	// String skripta;

	public MongoDBConnection() {
		super();
		// this.skripta = "";
		this.mongoClient = new MongoClient();
	}

	/**
	 * Funkcija koja se spaja na bazu ili u sluèaju da baza ne postoji kreira
	 * bazu po zadanom nazivu
	 * 
	 * @param nazivBaze
	 */
	public void spojiSeNaMongoDBBazu(String nazivBaze) {
		mongoDB = mongoClient.getDatabase(nazivBaze);
	}

	/**
	 * Funkcija koja zatvara vezu sa MongoDB bazom te sprema sve komande koje su
	 * joj poslane u txt datoteka zadanog imena
	 * 
	 * @param nazivBaze
	 *            koristi se za naziv txt datoteke
	 */
	public void zatvoriVezuSaBazom(String nazivBaze) {
		mongoClient.close();
		// CreateMongoDBScript csdbs = new CreateMongoDBScript();
		// csdbs.spremiSkriptu(skripta, nazivBaze);
	}

	/**
	 * Funkcija koja kreira kolekciju u MongoDB ili javlja kako navedena
	 * kolekcija veæ postoji. Kolekcija predstavlja tablicu iz SQL-a.
	 * 
	 * @param nazivKolekcije
	 */
	public void kreirajKolekciju(String nazivKolekcije) {
		try {
			mongoDB.createCollection(makniTocke(nazivKolekcije));
			// skripta += "db.createCollection(\"" + makniTocke(nazivKolekcije)
			// + "\")\n";
		} catch (Exception e) {
			System.out.println("Kolekcija veæ postoji!");
		}
	}

	/**
	 * Funkcja koja prolazi po redovima iiz sqla i za svaki red dodaje Dokument
	 * u MongoDB bazu. Dokument predstavlja red u tablici iz SQL-a.
	 * 
	 * @param nazivKolekcije
	 * @param listaStupaca
	 * @param listaRedaka
	 */
	public void dodajDokument(String nazivKolekcije, List<TipStupcaTablice> listaStupaca,
			List<List<String>> listaRedaka) {
		// skripta += "db." + nazivKolekcije + ".insert([\n";
		List<Document> listaDokumenata = new ArrayList<Document>();
		if (listaRedaka == null) {
			return;
		}
		if (listaStupaca == null) {
			return;
		}
		for (List<String> red : listaRedaka) {
			Document doc = new Document();
			for (int i = 0; i < listaStupaca.size(); i++) {
				doc.append(listaStupaca.get(i).getField(), red.get(i));
			}
			listaDokumenata.add(doc);
			// skripta += srediString(doc.toString()) + ",\n";
		}
		if (listaDokumenata.size() == 0) {
			return;
		}
		mongoDB.getCollection(makniTocke(nazivKolekcije)).insertMany(listaDokumenata);
		// skripta += "])\n";
	}

	/**
	 * Funkcija za ispis kolekcije
	 */
	public void ispisiNekiCollection(String nazivKolekcije) {
		System.out.println("ISPIS KOLEKCIJE");
		FindIterable<Document> iterable = mongoDB.getCollection(nazivKolekcije).find();
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document);
			}
		});
	}

	/**
	 * Funkcija za ubacivanje prikupljenih podataka iz SQL-a u MongoDB
	 * 
	 * @param podaciTablica
	 */
	/*
	 * public void ubaciPodatkeTablica(List<PodaciTablice> podaciTablica) { for
	 * (PodaciTablice pt : podaciTablica) {
	 * kreirajKolekciju(pt.getNazivTablice());
	 * dodajDokument(makniTocke(makniTocke(pt.getNazivTablice())),
	 * pt.getListaStupacaTablice(), pt.getListaRedakaTablice()); } }
	 */

	/**
	 * Funkcija koja mièe toèke iz naziva tablica
	 * 
	 * @param s
	 * @return
	 */
	public String makniTocke(String s) {
		String vrati = s.replaceAll("\\.", "");
		return vrati;
	}

	/**
	 * Funkcija koja uzima string koji predstavlja dokument koji se sprema te ga
	 * obraðuje u format koji se može izvršiti u MongoDB Shellu
	 * 
	 * @param s
	 * @return
	 */
	public String srediString(String s) {
		String s2 = s.substring(9);
		String s3 = s2.substring(0, s2.length() - 1);
		String s4 = s3.replaceAll("=", "=\"");
		String s5 = s4.replaceAll(",", "\",");
		String s6 = s5.substring(0, s5.length() - 1) + "\"}";
		String s7 = s6.replaceAll("=", ":");
		return s7;
	}

}
