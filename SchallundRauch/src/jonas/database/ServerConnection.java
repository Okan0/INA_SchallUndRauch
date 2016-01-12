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
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;


public class ServerConnection {
	
	public static void main(final String[] args)throws UnknownHostException {
   
	// connect to the local database server
    MongoClient mongoClient = new MongoClient();

    /*
    // Authenticate - optional
    MongoCredential credential = MongoCredential.createMongoCRCredential(userName, database, password);
    MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
    */

    // get handle to "Schall&Rauch"
    DB db = mongoClient.getDB("Schall&Rauch");

    // get a list of the collections in this database and print them out
    Set<String> collectionNames = db.getCollectionNames();
    for (final String s : collectionNames) {
        System.out.println(s);
    }

    // get a collection object to work with
    DBCollection coll = db.getCollection("testCollection");

    // drop all the data in it
    coll.drop();

    // make a document and insert it
    BasicDBObject doc = new BasicDBObject("name", "MongoDB")
            .append("type", "database")
            .append("count", 1)
            .append("info", new BasicDBObject("x", 203).append("y", 102));

    coll.insert(doc);

    // get it (since it's the only one in there since we dropped the rest earlier on)
    DBObject myDoc = coll.findOne();
    System.out.println(myDoc);

    // now, lets add lots of little documents to the collection so we can explore queries and cursors
    for (int i = 0; i < 100; i++) {
        coll.insert(new BasicDBObject().append("i", i));
    }
    System.out.println("total # of documents after inserting 100 small ones (should be 101) " + coll.getCount());

    // lets get all the documents in the collection and print them out
    DBCursor cursor = coll.find();
    try {
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    } finally {
        cursor.close();
    }

    // Count all documents in a collection but take a maximum second to do so
    coll.find().maxTime(1, SECONDS).count();

    // Bulk operations
    BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
    builder.insert(new BasicDBObject("_id", 1));
    builder.insert(new BasicDBObject("_id", 2));
    builder.insert(new BasicDBObject("_id", 3));

    builder.find(new BasicDBObject("_id", 1)).updateOne(new BasicDBObject("$set", new BasicDBObject("x", 2)));
    builder.find(new BasicDBObject("_id", 2)).removeOne();
    builder.find(new BasicDBObject("_id", 3)).replaceOne(new BasicDBObject("_id", 3).append("x", 4));

    BulkWriteResult result = builder.execute();
    System.out.println("Ordered bulk write result : " + result);

    // Unordered bulk operation - no guarantee of order of operation
    builder = coll.initializeUnorderedBulkOperation();
    builder.find(new BasicDBObject("_id", 1)).removeOne();
    builder.find(new BasicDBObject("_id", 2)).removeOne();

    result = builder.execute();
    System.out.println("Ordered bulk write result : " + result);

    // parallelScan
    ParallelScanOptions parallelScanOptions = ParallelScanOptions
            .builder()
            .numCursors(3)
            .batchSize(300)
            .build();

    List<Cursor> cursors = coll.parallelScan(parallelScanOptions);
    for (Cursor pCursor: cursors) {
        while (pCursor.hasNext()) {
            System.out.println(pCursor.next());
        }
    }

    // release resources
    db.dropDatabase();
    mongoClient.close();
}
// CHECKSTYLE:ON
}

