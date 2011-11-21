package databases;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;



public class Neo4jDB implements DB{

	EmbeddedGraphDatabase t;
	private double totalTime = 0;
	static Random random=new Random();

	void initialize(){

		t = new EmbeddedGraphDatabase("neo4j4");

	}


	public boolean delete(int in) {


		long start = System.currentTimeMillis();    

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

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return true;
	}

	public HashMap<String, String> readNode(int in) {

		HashMap<String, String> hm = new HashMap<String, String>();	
		long start = System.currentTimeMillis();  
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

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return hm;
	}
	
	public boolean update(int in) {
		long start = System.currentTimeMillis();  
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

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return true;
	}

	public Vector<HashMap<String,String>> getFollowers(int id) {

		long start = System.currentTimeMillis();  
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
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return results;
	}


	protected PathFinder<Path> instantiatePathFinder( int maxDepth )
	{
		return GraphAlgoFactory.allSimplePaths( Traversal.expanderForAllTypes(), maxDepth );
	}

	public void degreeOfSeparation(int startNode, int endNode , int maxPathLenght){

		long start = System.currentTimeMillis();  
		PathFinder<Path> finder = instantiatePathFinder( 10 );
		Iterable<Path> paths = finder.findAllPaths( t.getNodeById( startNode ), t.getNodeById( endNode) );

		for (Path p : paths)
		{
			System.out.println(p);
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
	}


	public Vector<HashMap<String,Long>> getFollowersOnlyIds(int id) {

		long start = System.currentTimeMillis();  
		Vector<HashMap<String, Long>> results = new Vector<HashMap<String, Long>>();

		Node node =  t.getNodeById(id);
		for ( Relationship rel : node.getRelationships(DynamicRelationshipType
				.withName("FRIENDS") ) )
		{
			HashMap<String, Long> hm = new HashMap<String, Long>();	
			Node otherNode = rel.getEndNode();
			hm.put("id",otherNode.getId());	    
			System.out.println("Neo4j id back is :" + otherNode.getId());
			results.add(hm);	
		}


		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return results;
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


	public void getTotal(int totalOps) {
		System.out.println(String.valueOf((totalTime * 100.0)  / totalOps));		

	}


/*
	public static void main(String[] args){

		Neo4jDB n = new Neo4jDB();
		n.initialize();
		n.degreeOfSeparation(2, 10, 10);
	}
*/

}

