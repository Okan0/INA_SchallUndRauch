package jonas.database;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;


public class ServerConnection {
	
	public ServerConnection()
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		if(coll.findOne() == null)
		{
			BasicDBObject doc = new BasicDBObject("name", "Schall&RauchDB")
		            .append("type", "database")
		            .append("count", 1)
		            .append("info", new BasicDBObject("x", 203).append("y", 102));
	
		    coll.insert(doc);
		    System.out.println("Ein Dokument wurde erstellt.");
		    mongoClient.close();
		}
		else
			System.out.println("Ein Dokument ist vorhanden.");
		
	}
	public void find(int offsetUpper,int offsetLower, double value)
	{
		if(value<0)
		{
			
		}
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		BasicDBObject oU = new BasicDBObject();
		oU.put("offsetUpper", offsetUpper);
		BasicDBObject oL = new BasicDBObject();
		oL.put("offsetLower", offsetLower);
		BasicDBObject v = new BasicDBObject();
		v.put("value", value);
		  
		DBCursor cursor = coll.find(oU, oL);
		if(cursor.hasNext())
			System.out.println("Element gefunden: ");
		while (cursor.hasNext()) {
		      System.out.println(cursor.next());
		}
		//Folgende Datenbank abfrage verwenden:
			//			"SELECT * FROM tbl_frequencies hz WHERE hz.frequency <= " 
			//			+ (freq + offset).toString() + " AND hz.frequency >= "
			//			+ (freq - offset).toString() + ";"
		
		mongoClient.close();
	}
	public void find(String data)
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		BasicDBObject b = new BasicDBObject();
		b.put("data", data);
		
		DBCursor cursor = coll.find(b);
		if(cursor.hasNext())
			System.out.println("Element gefunden: ");
		while (cursor.hasNext()) {
		      System.out.println(cursor.next());
		}
		System.out.println();
		mongoClient.close();
	}
	public void safe(String data)
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		coll.insert(new BasicDBObject().append("data", data));
		mongoClient.close();
	}
	public void update(long id, String newData)
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		// Datenzeile LÃ¶schen
		BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
		BulkWriteResult result;
		builder.find(new BasicDBObject("data", 2)).updateOne(new BasicDBObject("$set", new BasicDBObject("data", newData)));
		result = builder.execute();
		mongoClient.close();
	}
	public void delete(long data)
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		// Datenzeile LÃ¶schen
		BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
		BulkWriteResult result;
		builder.find(new BasicDBObject("data", data)).removeOne();;
		result = builder.execute();
		
		mongoClient.close();
	}
	public void delete(String data)
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		// Datenzeile LÃ¶schen
		BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
		BulkWriteResult result;
		builder.find(new BasicDBObject("data", data)).removeOne();;
		result = builder.execute();
		
		mongoClient.close();
	}
	public Set<String> collectionNames()
	{
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		DB db = mongoClient.getDB("Schall&Rauch");
		Set<String> collectionNames = db.getCollectionNames();
		return(collectionNames);
	}
	public void dropDataInCollection()
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
	    // drop all the data in it
		coll.drop();
		mongoClient.close();
	}
	public void createTestData()
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		// add 5 little documents to the collection
		for (int i = 0; i < 5; i++) 
			coll.insert(new BasicDBObject().append("data", i));
	    
		mongoClient.close();
	}
	public void print()
	{
		MongoClient mongoClient = null;
		try {
			// connect to the local database server
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			System.out.println("Verbindung Fehlgeschlagen");
			e.printStackTrace();
		}
		// get handle to "Schall&Rauch"
		DB db = mongoClient.getDB("Schall&Rauch");
		// get a collection object to work with
		DBCollection coll = db.getCollection("SoundCollection");
		
		
		// parallelScan
	    ParallelScanOptions parallelScanOptions = ParallelScanOptions
	            .builder()
	            .numCursors(3)
	            .batchSize(300)
	            .build();
	    try{
	    List<Cursor> cursors = coll.parallelScan(parallelScanOptions);
	    for (Cursor pCursor: cursors) {
	        while (pCursor.hasNext()) {
	            System.out.println(pCursor.next());
	        }
	    }
	    }
	    catch(RuntimeException e)
	    {
	    	System.out.println("Keine Daten vorhanden.");
			e.printStackTrace();
	    }
	    mongoClient.close(); 
	}
	
	public static void main(final String[] args)throws UnknownHostException {
   
	ServerConnection sc = new ServerConnection();

    for(String s :sc.collectionNames())
    		System.out.println("Collections: " +s);
    
    sc.dropDataInCollection();
    sc.createTestData();
    
    sc.print();
    System.out.println();
    
    sc.update(2, "hallo");
    sc.delete(3);
    
    sc.print();
    
    sc.find("hallo");
}
// CHECKSTYLE:ON
}

