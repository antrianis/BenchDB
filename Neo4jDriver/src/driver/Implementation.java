package driver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.Traversal;


public class Implementation {

	GraphDatabaseService t;	
	static Random random=new Random();



	void initialize(GraphDatabaseService graphDbService){
		t = graphDbService; 
	}


	public boolean delete(int in) {



		Transaction tx = t.beginTx();
		try
		{
			try{
			t.getNodeById(in).delete();
			}
			catch(Exception e )
			{
				
				System.out.println("ex while del");
			}
			tx.success();
		}
		finally
		{
			tx.finish();
		}


		return true;
	}

	public HashMap<String, String> readNode(int in) {

		HashMap<String, String> hm = new HashMap<String, String>();	
		Transaction tx = t.beginTx();
		try
		{


			//			hm.put("id", String.valueOf(ri.next().getStartNode().getId()));
			Node n = t.getNodeById(in);
			hm.put("nickname", n.getProperty("nickname").toString());
			hm.put("password", n.getProperty("password").toString());
			hm.put("email", n.getProperty("email").toString());

			tx.success();
		}
		finally
		{
			tx.finish();
		}

		return hm;
	}
	
	public boolean update(int in) {
		Transaction tx = t.beginTx();
		try
		{

			Node n = t.getNodeById(in);
			n.setProperty("nickname", ASCIIString(20)); //$NON-NLS-1$
			n.setProperty("password", ASCIIString(20)); //$NON-NLS-1$
			n.setProperty("email", ASCIIString(20)); //$NON-NLS-1$
			tx.success();
		}
		finally
		{
			tx.finish();
		}


		return true;
	}

	
	public Vector<HashMap<String,String>> getFollowers(int id) {
		
		Vector<HashMap<String, String>> results = new Vector<HashMap<String, String>>();
		Iterable<Relationship> r = t.getNodeById(id).getRelationships(DynamicRelationshipType
				.withName("FRIENDS"));
		Iterator<Relationship> ri = r.iterator();

		while(ri.hasNext()){
			HashMap<String, String> hm = new HashMap<String, String>();	

			Node n = ri.next().getStartNode();
//			hm.put("id", String.valueOf(ri.next().getStartNode().getId()));

			hm.put("nickname",n.getProperty("nickname").toString());
			hm.put("password", n.getProperty("password").toString());
			hm.put("email", n.getProperty("email").toString());

			results.add(hm);	
		}
		return results;
	}

	
	public Vector<HashMap<String,String>> getFollowersOnlyIds(int id) {

		Vector<HashMap<String, String>> results = new Vector<HashMap<String, String>>();
		
		Iterable<Relationship> r = t.getNodeById(id).getRelationships(DynamicRelationshipType
				.withName("FRIENDS"));  //DIRECTION!!!
		Iterator<Relationship> ri = r.iterator();



		while(ri.hasNext()){
			HashMap<String, String> hm = new HashMap<String, String>();	

			Node n = ri.next().getStartNode();
			hm.put("id", String.valueOf(n.getId()));

			//System.out.println("neo4j id back is :" + n.getId());
			results.add(hm);	
		}
	
		return results;
	}
	
	protected PathFinder<Path> instantiatePathFinder( int maxDepth )
	{
		return GraphAlgoFactory.allSimplePaths( Traversal.expanderForAllTypes(), maxDepth );
	}

	public Iterable<Path> degreeOfSeparation(int startNode, int endNode , int maxPathLenght){
		
		//printAllGraphNodes(t);
		
		PathFinder<Path> finder = instantiatePathFinder( maxPathLenght );
		Iterable<Path> paths = finder.findAllPaths( t.getNodeById( startNode ), t.getNodeById( endNode) );

	//	for (Path p : paths)
	//	{
	//		System.out.println(p);
	//	}
		
		return null;
		//return paths;
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
		public void warmCache(GraphDatabaseService graphDb) {

		Iterator<Node> nodes = graphDb.getAllNodes().iterator();

		Transaction tx = graphDb.beginTx();
		try {
			while (nodes.hasNext()) {
				Node currentNode = nodes.next();
				
				currentNode.getId(); //$NON-NLS-1$
				

			}
			tx.success();

		} finally {
			tx.finish();

		}

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
