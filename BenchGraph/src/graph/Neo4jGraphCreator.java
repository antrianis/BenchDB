package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;
import org.neo4j.kernel.impl.batchinsert.BatchInserterImpl;

public class Neo4jGraphCreator {



	static Random random=new Random();
	private static String db = "NE04J";
	private static BufferedReader in;
	private static BatchInserter inserter;
	private static GraphDatabaseService t;
	static int totalNodes;
	static int totalRels;
	static Scanner st = null;
	private static long totalRelElapsed = 0;
	private static long totalAddNodeElapsed = 0;
	
	public static void main(String[] args)  {

		totalNodes = Integer.parseInt(args[0]);
		
		inserter = new BatchInserterImpl("../Neo4jDriver/db");


		try {
			st = new Scanner(new File("followers.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		createNodes();
		addEdgesBetweenUsers();

		inserter.shutdown(); 		// shutdown, makes sure all changes are written to disk

//		System.out.println((totalAddNodeElapsed * 100.0)  / (totalRels + totalNodes));
	
		//writeToFile( String.valueOf((totalRelElapsed * 100.0)  / totalRels) , "addRel");

	//	writeToFile(String.valueOf((totalAddNodeElapsed * 100.0) / totalNodes), "addNode");
		
//		t = new EmbeddedGraphDatabase("/data/Neo4jDriver/db");
//		printAllGraphNodes(t);
//		t.shutdown();

	}

	private static void createNodes() {

		int i = 1;


		while(i++ <= totalNodes)
		{

			addNode(i);

		}



	}

	private static void addEdgesBetweenUsers() {


		while(true)
		{

			try{
				long ida = st.nextLong();
				long idb = st.nextLong();
				addEdge(idb,ida);

			}catch(NoSuchElementException e){

				break;
			}


		}
	}
	public static String ASCIIString(int length)
	{
		int interval='~'-' '+1;

		byte []buf = new byte[length];
		random.nextBytes(buf);
		for (int i = 0; i < length; i++) {
			if (buf[i] < 0) {
				buf[i] = (byte)((-buf[i] % interval) + ' ');
			} else {
				buf[i] = (byte)((buf[i] % interval) + ' ');
			}
		}
		return new String(buf);
	}

	public static void addNode(int i){

		long start = System.currentTimeMillis();
		Map<String, Object> properties = new HashMap<String, Object>();

		try {
			properties.put("id", i); //$NON-NLS-1$
			properties.put("nickname", ASCIIString(20)); //$NON-NLS-1$
			properties.put("password", ASCIIString(20)); //$NON-NLS-1$
			properties.put("email", ASCIIString(20)); //$NON-NLS-1$

		} catch (IllegalArgumentException e) {
			System.out.println(e);

		}

		//	long userID =
		    

		inserter.createNode(properties);

		long elapsedTime = System.currentTimeMillis() - start;
		totalAddNodeElapsed += elapsedTime;
		
		//writeToFile( String.valueOf(elapsedTime),"addNode");
		
		
		//user.id = userID;


	}

	public static void addEdge(Long fromID, Long toID) {

		long start = System.currentTimeMillis();    

		inserter.createRelationship(fromID, toID, DynamicRelationshipType
				.withName("FRIENDS"), null); //$NON-NLS-1$

		long elapsedTime = System.currentTimeMillis() - start;
		totalAddNodeElapsed += elapsedTime;

	}


public static void printAllGraphNodes(GraphDatabaseService graphDb) {

	Iterator<Node> nodes = graphDb.getAllNodes().iterator();

	Transaction tx = graphDb.beginTx();
	try {
		while (nodes.hasNext()) {
			Node currentNode = nodes.next();

			System.out.println("NODE: " + currentNode.getId()); //$NON-NLS-1$

			System.out.println("NODE PROPERTIES"); //$NON-NLS-1$
			Iterator<String> keys = currentNode.getPropertyKeys()
			.iterator();

			while (keys.hasNext()) {
				String currentkey = keys.next();
				System.out.print("Key: " + currentkey.toString()); //$NON-NLS-1$
				System.out
				.println("  Value: " + currentNode.getProperty(currentkey.toString())); //$NON-NLS-1$
			}

			Iterator<Relationship> relations = currentNode
			.getRelationships().iterator();
			System.out.println("NODE RELATIONS"); //$NON-NLS-1$
			while (relations.hasNext()) {
				Relationship currentRelation = relations.next();
				System.out
				.println("Relation Name: " + currentRelation.getType().name()); //$NON-NLS-1$
				System.out
				.println("Relation node id end node: " + currentRelation.getEndNode().getId()); //$NON-NLS-1$
				System.out
				.println("Relation node id other node: " + currentRelation.getOtherNode(currentRelation.getEndNode()).getId()); //$NON-NLS-1$
			}

			System.out.println();

		}
		tx.success();

	} finally {
		tx.finish();

	}

}
}
