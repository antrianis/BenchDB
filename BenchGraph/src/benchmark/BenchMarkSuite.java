package benchmark;


import generators.UniformIntegerGenerator;

import java.sql.SQLException;
import java.util.Iterator;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import databases.MySQLDB;
import databases.Neo4jSocketDB;

public class BenchMarkSuite {

	private  int totalOps;
	private  Integer testCase;
	private  Neo4jSocketDB n = new Neo4jSocketDB(); 		//Neo4jDB n = new Neo4jDB();
	private  MySQLDB s;
	private String db;
	private  int totalNodes;		
	private Integer mySqlSchema;
	private int PATH_SIZE = 2;
	public static void main(String[] args) throws SQLException  {

		BenchMarkSuite b = new BenchMarkSuite();

		b.testCase = Integer.parseInt(args[0]);
		b.totalOps = Integer.parseInt(args[1]);		
		b.totalNodes = Integer.parseInt(args[2]);
		b.db = 	args[3];
		b.mySqlSchema = Integer.parseInt(args[4]);


		if (b.db.equals("neo4j")) //$NON-NLS-1$
			b.neo4jBench();
		else if (b.db.equals("mysql")) //$NON-NLS-1$
			b.mysqlBench();
		else
		{
			System.err.println("No such db");
			System.exit(-1);		
		}
		b.printResult(b.db);
	}

	private  void printResult(String db) {
		System.out.println(db);
		if (db.equals("neo4j"))
			this.n.getTotalTime(this.totalOps);
		else
			this.s.getTotalTime(this.totalOps);
	}

	private  void neo4jBench() {

		this.n.initialize();

		UniformIntegerGenerator g = new UniformIntegerGenerator(1,totalNodes); // 1 - 1000
		int in = g.nextInt();	
		int in2 = 0 ;
		for(int i = 0; i <this.totalOps; i++){

			if (this.testCase == 5)
				in2 = g.nextInt();
			executeFunctionNeo4j(in,in2);
			in = g.nextInt();
		}

	}

	private void mysqlBench() throws SQLException  {
		this.s = new MySQLDB();
		try {
			this.s.initialize();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		UniformIntegerGenerator g = new UniformIntegerGenerator(0,totalNodes - 1); //0 - 999 
		int in = g.nextInt();	
		int in2 = 0 ;
		for(int i = 0; i < this.totalOps; i++){

			if (this.testCase == 5)
				in2 = g.nextInt();
			executeFunctionMySQL(in,in2);
			in = g.nextInt();
		}


	}

	private  void executeFunctionNeo4j(int in1,int in2) {
		
		
		switch (this.testCase)
		{
		case 0:{ this.n.getFollowersOnlyIds(in1); break;}
		case 1:{ this.n.getFollowers(in1); break;}
		case 2:{ this.n.delete(in1); break;}
		case 3:{ this.n.readNode(in1); break;}
		case 4:{ this.n.update(in1); break;}
		case 5:{ this.n.degreeOfSeparation(in1,in2,PATH_SIZE); break;}

		}

	}

	private void executeFunctionMySQL(int in1,int in2) throws SQLException {

		
		
		int newCase = this.testCase;	
		if (this.testCase == 0)
			if (mySqlSchema == 2)
				newCase = 1;

		switch (newCase)
		{
		case 0:{ this.s.getFollowersOneTableOnlyIds(in1); break;}
		case 1:{ this.s.getFollowersManyOnlyIds(in1); break;}		
		case 2:{ this.s.delete(in1); break;}
		case 3:{ this.s.readNode(in1); break;}
		case 4:{ this.s.update(in1); break;}
		case 5:{ this.s.degreeOfSeparation(in1,in2,PATH_SIZE); break;}
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
}
