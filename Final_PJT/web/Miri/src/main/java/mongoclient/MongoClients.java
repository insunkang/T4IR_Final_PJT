package mongoclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoClients {

	public static void main(String[] args) {
		String MongoDB_IP = "127.0.0.1";
		int MongoDB_PORT = 27017;
		String DB_NAME = "km";

		// Connect to MongoDB
		MongoClient mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));

		// View Database List
		List<String> databases = mongoClient.getDatabaseNames();

		System.out.println("====Database List======");
		int num = 1;
		for (String dbName : databases) {
			System.out.println(num + ". " + dbName);
			num++;
		}

		System.out.println();

		// Connect Database and Show Collection List in Database
		DB db = mongoClient.getDB(DB_NAME);
		Set<String> collections = db.getCollectionNames();

		System.out.println("Database: " + DB_NAME);
		for (String colName : collections) {
			System.out.println("+Collection: " + colName);
		}

		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		MongoCollection<Document> collection = database.getCollection("km");

		// mongoDB insert
		/*
		 * Document doc=new Document("name","MongoDB"); collection.insertOne(doc);
		 */

		// mongoDB find
		/*
		 * MongoCursor<Document> cursor=collection.find().iterator(); try {
		 * while(cursor.hasNext()) { System.out.println(cursor.next().toJson()); }
		 * }finally { cursor.close(); }
		 */

		// collection의 첫번째 출력
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());

		System.out.println("========================================");

		// Filter 적용해서 출력하기 밑은 speech가 "안녕"인 것만 출력하기
		Filters filters = null;
		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(Document document) {
				System.out.println(document.toJson());
			}
		};
		collection.find(filters.eq("speech", "안녕")).forEach(printBlock);
	}
}
