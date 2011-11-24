package benchmark;

import generators.UniformIntegerGenerator;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import databases.MySQLDB;
import databases.Neo4jSocketDB;
import databases.DB;

public class BenchMarkSuite {

	private static boolean shouldStop;
	private int totalOps;
	private Integer testCase;
	private Neo4jSocketDB n = new Neo4jSocketDB(); // Neo4jDB n = new Neo4jDB();
	private MySQLDB s;
	private String db;
	private int totalNodes;
	private Integer mySqlSchema;
	private int PATH_SIZE = 2;
	private int clientNumber;
	private int opsPerClient;
	private int maxExecutionTime;

	public static void main(String[] args) throws SQLException {

		BenchMarkSuite b = new BenchMarkSuite();

		b.testCase = Integer.parseInt(args[0]);
		b.totalOps = Integer.parseInt(args[1]);
		b.totalNodes = Integer.parseInt(args[2]);
		b.db = args[3];
		b.mySqlSchema = Integer.parseInt(args[4]);
		b.clientNumber = Integer.parseInt(args[5]);
		b.maxExecutionTime = Integer.parseInt(args[6]);

		b.opsPerClient = b.totalOps / b.clientNumber;

		Vector<Thread> threads = new Vector<Thread>();
		for (int threadid = 0; threadid < b.clientNumber; threadid++) {

			Thread t = new ClientThread(b.db, threadid, b.clientNumber,
					b.opsPerClient, b);// ,targetperthreadperms);

			threads.add(t);

		}

		long st = System.currentTimeMillis();

		for (Thread t : threads) {
			t.start();
		}

		Thread terminator = null;

		terminator = new TerminatorThread(b.maxExecutionTime, threads, b);
		terminator.start();

		int opsDone = 0;
		int totalLatency = 0;
		for (Thread t : threads) {
			try {
				t.join();
				opsDone += ((ClientThread) t).getOpsDone();
				totalLatency += ((ClientThread) t).getTotalLatency();
			} catch (InterruptedException e) {
			}
		}

		long en = System.currentTimeMillis();

		exportResults(en - st, opsDone, totalLatency);

		//		if (b.db.equals("neo4j")) //$NON-NLS-1$
		// b.neo4jBench();
		//		else if (b.db.equals("mysql")) //$NON-NLS-1$
		// b.mysqlBench();
		// else
		// {
		// System.err.println("No such db");
		// System.exit(-1);
		// }
		// b.printResult(b.db);
	}

	boolean isStopRequested() {
		return shouldStop;

	}

	void stopRequested() {
		shouldStop = true;

	}

	private static void exportResults(long totalTime, int opsDone,
			int totalLatency) {

		System.out.println("TotalTime is :" + totalTime + "ms");
		System.out.println("Total Ops done  :" + opsDone);
		System.out.println("Throughput is : (opsdone/totaltime) "
				+ (double) opsDone / (double) totalTime);
		System.out.println("Average Latency is : (totalLatency/opsDone) "
				+ (double) totalLatency / (double) opsDone);

	}

	private void printResult(String db) {
		System.out.println(db);
		if (db.equals("neo4j"))
			this.n.getTotalTime(this.totalOps);
		else
			this.s.getTotalTime(this.totalOps);
	}

//	private void neo4jBench() {
//
//		this.n.initialize();
//
//		UniformIntegerGenerator g = new UniformIntegerGenerator(1, totalNodes); // 1
//		// -
//		// 1000
//		int in = g.nextInt();
//		int in2 = 0;
//		for (int i = 0; i < this.totalOps; i++) {
//
//			if (this.testCase == 5)
//				in2 = g.nextInt();
//			executeFunctionNeo4j(in, in2);
//			in = g.nextInt();
//		}
//
//	}

//	private void mysqlBench() throws SQLException {
//		this.s = new MySQLDB();
//		try {
//			this.s.initialize();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		UniformIntegerGenerator g = new UniformIntegerGenerator(0,
//				totalNodes - 1); // 0 - 999
//		int in = g.nextInt();
//		int in2 = 0;
//		for (int i = 0; i < this.totalOps; i++) {
//
//			if (this.testCase == 5)
//				in2 = g.nextInt();
//			executeFunctionMySQL(in, in2);
//			in = g.nextInt();
//		}
//
//	}

	private void executeFunctionNeo4j(int in1, int in2,Neo4jSocketDB n) {

		switch (this.testCase) {
		case 0: {
			n.getFollowersOnlyIds(in1);
			break;
		}
		case 1: {
			n.getFollowers(in1);
			break;
		}
		case 2: {
			n.delete(in1);
			break;
		}
		case 3: {
			n.readNode(in1);
			break;
		}
		case 4: {
			n.update(in1);
			break;
		}
		case 5: {
			n.degreeOfSeparation(in1, in2, PATH_SIZE);
			break;
		}

		}

	}

	private void executeFunctionMySQL(int in1, int in2, MySQLDB s) throws SQLException {

		int newCase = this.testCase;
		if (this.testCase == 0)
			if (mySqlSchema == 2)
				newCase = 1;

		switch (newCase) {
		case 0: {
			s.getFollowersOneTableOnlyIds(in1);
			break;
		}
		case 1: {
			s.getFollowersManyOnlyIds(in1);
			break;
		}
		case 2: {
			s.delete(in1);
			break;
		}
		case 3: {
			s.readNode(in1);
			break;
		}
		case 4: {
			s.update(in1);
			break;
		}
		case 5: {
			s.degreeOfSeparation(in1, in2, PATH_SIZE);
			break;
		}
		}

	}

	public static void printAllGraphNodes(GraphDatabaseService graphDb) {

		Iterator<Node> nodes = graphDb.getAllNodes().iterator();

		Transaction tx = graphDb.beginTx();
		try {
			while (nodes.hasNext()) {
				Node currentNode = nodes.next();

				System.out.println("NODE: " + currentNode.getId()); //$NON-NLS-1$

			}
			tx.success();

		} finally {
			tx.finish();

		}

	}

	public void doOperationNeo4j(Neo4jSocketDB n) {

		UniformIntegerGenerator g = new UniformIntegerGenerator(1, totalNodes); // 1
		// -
		// 1000
		int in = g.nextInt();
		int in2 = 0;

		if (this.testCase == 5)
			in2 = g.nextInt();
		executeFunctionNeo4j(in, in2,n);

	}

	public void doOperationMySQL(MySQLDB s) {

		UniformIntegerGenerator g = new UniformIntegerGenerator(0,
				totalNodes - 1); // 0 - 999
		int in = g.nextInt();
		int in2 = 0;

		if (this.testCase == 5)
			in2 = g.nextInt();
		try {
			executeFunctionMySQL(in, in2,s);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public DB initializeDB(String db) {

		if (db.equals("neo4j"))
		{
			Neo4jSocketDB neo4jDB = new Neo4jSocketDB();
			neo4jDB.initialize();
			return neo4jDB;
		}
		else if (db.equals("mysql")){
			MySQLDB sqlDB = null;
			sqlDB = new MySQLDB();
			try {
				sqlDB.initialize();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return sqlDB;
		}
		return null;
	}
}

class ClientThread extends Thread {
	static Random random = new Random();

	int _opcount;
	double _target = 0;;

	int _opsdone;
	int _threadid;
	int _threadcount;

	private String _db;

	int _latency;

	private BenchMarkSuite _b;

	/**
	 * Constructor.
	 * 
	 * @param db
	 *            the DB implementation to use
	 * @param dotransactions
	 *            true to do transactions, false to insert data
	 * @param workload
	 *            the workload to use
	 * @param threadid
	 *            the id of this thread
	 * @param threadcount
	 *            the total number of threads
	 * @param props
	 *            the properties defining the experiment
	 * @param opcount
	 *            the number of operations (transactions or inserts) to do
	 * @param targetperthreadperms
	 *            target number of operations per thread per ms
	 */
	// public ClientThread(String db, int threadid, int threadcount, int
	// opcount, double targetperthreadperms,TerminatorThread t)
	public ClientThread(String db, int threadid, int threadcount, int opcount,
			BenchMarkSuite b)

	{

		_db = db;
		_threadid = threadid;
		_opcount = opcount;
		_opsdone = 0;
		// _target=targetperthreadperms;
		// _threadcount=threadcount;
		_b = b;
	}

	public int getOpsDone() {
		return _opsdone;
	}

	public int getTotalLatency() {
		return _latency;
	}

	public void run() {
		// spread the thread operations out so they don't all hit the DB at the
		// same time
		try {
			// // GH issue 4 - throws exception if _target>1 because
			// random.nextInt
			// // argument must be >0
			// // and the sleep() doesn't make sense for granularities < 1 ms
			// // anyway
			if ((_target > 0) && (_target <= 1.0)) {
				sleep(random.nextInt((int) (1.0 / _target)));
			}
		} catch (InterruptedException e) {
			// do nothing.
		}
		DB t = _b.initializeDB(_db);
		
		while (((_opcount == 0) || (_opsdone < _opcount))
				&& !_b.isStopRequested()) {

			long st = System.currentTimeMillis();
			if (_db.equals("neo4j")) //$NON-NLS-1$
				_b.doOperationNeo4j((Neo4jSocketDB)t);
			else if (_db.equals("mysql")) //$NON-NLS-1$
				_b.doOperationMySQL((MySQLDB)t);

			long end = System.currentTimeMillis();

			_latency += end - st;

			_opsdone++;

			// throttle the operations
			if (_target > 0) {
				// //this is more accurate than other throttling approaches we
				// have
				// tried,
				// //like sleeping for (1/target throughput)-operation latency,
				// //because it smooths timing inaccuracies (from sleep() taking
				// an
				// int,
				// //current time in millis) over many operations
				while (System.currentTimeMillis() - st < ((double) _opsdone)
						/ _target) {
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// do nothing.
					}
				}
			}
		}
	}
}

class TerminatorThread extends Thread {

	private Vector<Thread> threads;
	private long maxExecutionTime;
	private long waitTimeOutInMS;
	private BenchMarkSuite _b;

	public TerminatorThread(long maxExecutionTime, Vector<Thread> threads,
			BenchMarkSuite b) {

		_b = b;
		this.maxExecutionTime = maxExecutionTime;
		this.threads = threads;
		waitTimeOutInMS = 2000;
		System.err.println("Maximum execution time specified as: "
				+ maxExecutionTime + " secs");
	}

	public void run() {
		try {
			Thread.sleep(maxExecutionTime * 1000);
		} catch (InterruptedException e) {
			System.err
			.println("Could not wait until max specified time, TerminatorThread interrupted.");
			return;
		}

		System.err
		.println("Maximum time elapsed. Requesting stop for the workload.");

		_b.stopRequested();

		System.err.println("Stop requested for workload. Now Joining!");
		for (Thread t : threads) {
			while (t.isAlive()) {
				try {
					t.join(waitTimeOutInMS);
					if (t.isAlive()) {
						System.err.println("Still waiting for thread "
								+ t.getName() + " to complete. ");
					}
				} catch (InterruptedException e) {
					// Do nothing. Don't know why I was interrupted.
				}
			}
		}
	}
}
