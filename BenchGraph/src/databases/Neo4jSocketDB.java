package databases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import driver.ClientDriver;

public class Neo4jSocketDB implements DB {

	EmbeddedGraphDatabase t;
	private double totalTime = 0;
	static Random random = new Random();
	private ClientDriver d;

	public void initialize() {

		this.d = new ClientDriver();
		this.d.connect();
//		Collection<Integer> a = new ArrayList<Integer>();
//		a.add(5);
//		this.d.executeQuery(a);

	}

	public void closeConnection(){
		
		this.d.closeConnection();
		
	}
	
	public boolean delete(int in) {

		long start = System.currentTimeMillis();
		Collection<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(in);
		d.executeQuery(a);

		long elapsedTime = System.currentTimeMillis() - start;
		this.totalTime += elapsedTime;

		return true;
	}

	public HashMap<String, String> readNode(int in) {

		HashMap<String, String> hm = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		Collection<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(in);
		this.d.executeQuery(a);

		long elapsedTime = System.currentTimeMillis() - start;
		this.totalTime += elapsedTime;

		return hm;
	}

	public boolean update(int in) {
		long start = System.currentTimeMillis();
		
		Collection<Integer> a = new ArrayList<Integer>();
		a.add(2);
		a.add(in);
		this.d.executeQuery(a);
		
		
		long elapsedTime = System.currentTimeMillis() - start;
		this.totalTime += elapsedTime;
		return true;
	}

	public Vector<HashMap<String, String>> getFollowers(int id) {

		long start = System.currentTimeMillis();
		Vector<HashMap<String, String>> results = new Vector<HashMap<String, String>>();

		Collection<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(id);
		this.d.executeQuery(a);

		this.d.executeQuery(a);

		long elapsedTime = System.currentTimeMillis() - start;
		this.totalTime += elapsedTime;

		return results;
	}

	public Vector<HashMap<String, String>> getFollowersOnlyIds(int id) {

		long start = System.currentTimeMillis();
		Vector<HashMap<String, String>> results = new Vector<HashMap<String, String>>();
		Collection<Integer> a = new ArrayList<Integer>();
		a.add(4);
		a.add(id);
		this.d.executeQuery(a);

		long elapsedTime = System.currentTimeMillis() - start;
		this.totalTime += elapsedTime;

		return results;
	}

	// public Vector<HashMap<String, String>> degreeOfSeparation(int id) {
	//
	// long start = System.currentTimeMillis();
	// Vector<HashMap<String, String>> results = new Vector<HashMap<String,
	// String>>();
	// Collection<Integer> a = new ArrayList<Integer>();
	// a.add(5);
	// a.add(id);
	// this.d.executeQuery(a);
	//
	// long elapsedTime = System.currentTimeMillis() - start;
	// this.totalTime += elapsedTime;
	//
	// return results;
	// }

	public void degreeOfSeparation(int startNode, int endNode, int maxPathLenght) {

		long start = System.currentTimeMillis();

		Collection<Integer> a = new ArrayList<Integer>();
		a.add(5);
		a.add(startNode);
		a.add(endNode);
		a.add(maxPathLenght);
		this.d.executeQuery(a);
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
	}

	public static String ASCIIString(int length) {
		int interval = '~' - ' ' + 1;

		byte[] buf = new byte[length];
		random.nextBytes(buf);
		for (int i = 0; i < length; i++) {
			if (buf[i] < 0) {
				buf[i] = (byte) ((-buf[i] % interval) + ' ');
			} else {
				buf[i] = (byte) ((buf[i] % interval) + ' ');
			}
		}
		return new String(buf);
	}

	public void getTotalTime(int totalOps) {

		System.out.println(String.valueOf((totalTime * 100.0) / totalOps));
	}

}
