package mongoServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoTest {
	String speech;
	String sysdate;
	
	public MongoTest(String speech, String sysdate) {
		super();
		this.speech = speech;
		this.sysdate = sysdate;
		
		String MongoDB_IP="127.0.0.1";
		int MongoDB_PORT=27017;
		String DB_NAME="testDB";
		
		//Connect to MongoDB
		MongoClient mongoClient=new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));
		
		//View Database List
		List<String> databases=mongoClient.getDatabaseNames();
		
		System.out.println("====Database List======");
		int num=1;
		for(String dbName:databases) {
			System.out.println(num+". "+dbName);
			num++;
		}
		
		System.out.println();
		
		//Connect Database and Show Collection List in Database
		DB db=mongoClient.getDB(DB_NAME);
		Set<String> collections=db.getCollectionNames();
		
		System.out.println("Database: "+DB_NAME);
		for(String colName:collections) {
			System.out.println("+Collection: "+colName);
		}
		
		MongoDatabase database=mongoClient.getDatabase(DB_NAME);
		MongoCollection<Document> collection=database.getCollection("speechData");
		
		Document doc=new Document("speech",speech).append("sysdata", sysdate);
		collection.insertOne(doc);
		
		/////////////////////////////////////////////////여기까지가 실시간 mongodb저장
		
		MongoCursor<Document> cursor=collection.find().iterator();
		
		String[] spl=null;
		MongoCollection<Document> collection1=database.getCollection("FilterData");
		cursor=collection.find().iterator();
		//Document doc;
		collection1.drop();
		while(cursor.hasNext()) {
			spl=cursor.next().toJson().split("\"");
			doc=new Document("speech_filter",spl[9]);
			collection1.insertOne(doc);
		}
		
		cursor=collection1.find().iterator();
		try {
			File file=new File("f:///mongtcsv"+".csv");
			if(file.exists()) {
				file.delete();
			}
			
			BufferedWriter fw=new BufferedWriter(new FileWriter("f:///mongtcsv"+".csv",true));
			while(cursor.hasNext()) {
				Document e=cursor.next();
				String x=e.getString("speech_filter");
				System.out.println("speech_filter :" +x);
				
				fw.write(x);
				fw.newLine();
				
			}
			fw.flush();
			fw.close();   
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}
