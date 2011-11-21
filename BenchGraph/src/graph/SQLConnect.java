package graph;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class SQLConnect
{
	static Random random=new Random();
	private static Scanner st;
	static Connection conn = null;
	private static int total;
	private static long totalAddEntryElapsed = 0;
	private static  String schema;
	public static void main (String[] args)
	{

		total = Integer.parseInt(args[0]);	
		schema = args[1];

		try
		{

			System.out.println("Workis static for one table");
			String userName = "socialUser";
			String password = "socialUser";
			String url = "jdbc:mysql://localhost/social";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			conn = DriverManager.getConnection (url, userName, password);
			System.out.println ("Database connection established");

			dropTables();
			createTables();


			try {
				st = new Scanner(new File("followers.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			createNodes();


			addEdgesBetweenUsers();



		}
		catch (Exception e)
		{
			System.err.println ("Cannot connect to database server" + e);
		}
		finally
		{
			if (conn != null)
			{
				try
				{
					conn.close ();
					System.out.println ("Database connection terminated");
				}
				catch (Exception e) { /* ignore close errors */ }
			}
		}

	}


	private static void dropTables() {

		Statement s = null;

		try {
			s = conn.createStatement ();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String tableQuery1 = "DROP DATABASE social;" ;

		String tableQuery2 = "CREATE DATABASE social" ;
		String tableQuery3 = "USE social" ;



		try {

			s.executeUpdate (tableQuery1);

			s.executeUpdate (

					tableQuery2		
			);

			s.executeUpdate (

					tableQuery3		
			);




		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.close ();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private static void addEdgesBetweenUsers() {
		//		if (!schema.equals("one")){
		//			System.out.println (" creating small tables ");
		//			createAllSmallTables();
		//			}

		while(true)
		{

			try{
				long ida = st.nextLong();
				long idb = st.nextLong();

				if (schema.equals("one")){

					addEntryOneTable(idb,ida);
				}
				else{
					manyTables(idb,ida);
				}	
				//addEdge(idb,ida);

			}catch(NoSuchElementException e){

				break;
			}


		}
		System.out.println (" added all friends ");

	}

	private static void addEdge(Long fromID, Long toID) {

		if (schema.equals("one"))
			oneTable(fromID,toID);
		else 
			manyTables(fromID,toID);


	}
	private static void createAllSmallTables() {

		for(long i = 0 ; i <= total   ; ++ i)
			createTable(i);
	}


	private static void oneTable(Long fromID, Long toID) {

		addEntryOneTable(fromID,toID);
	}

	private static void addEntryOneTable(Long fromID, Long toID) {

		int count = 0;
		String tableName = "friendship";
		String sql =  "INSERT INTO "+ tableName + " (to_ID,from_ID)"
		+ " VALUES (?,?)"; //$NON-NLS-1$
		//	long start = System.currentTimeMillis();  
		PreparedStatement prest=null;		
		try {

			prest = conn.prepareStatement(sql);
			prest.setLong(1,toID);
			prest.setLong(2,fromID);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int rs1 = 0;
		try {


			rs1 = prest.executeUpdate();


		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//System.out.println (rs1 + " rows were inserted"); //$NON-NLS-1$	
		try {
			prest.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void manyTables(Long fromID, Long toID) {
		if(!tableExists(toID))
		{
			createTable(toID);

		}

		addEntry(fromID,toID);
	}


	private static void addEntry(Long fromID,Long toID) {

		int count = 0;
		String tableName = "user" + toID;
		String sql =  "INSERT INTO "+ tableName + " (to_fk,from_fk)"
		+ " VALUES (?,?)"; //$NON-NLS-1$
		long start = System.currentTimeMillis();  
		PreparedStatement prest=null;		
		try {

			prest = conn.prepareStatement(sql);
			prest.setLong(1,toID);
			prest.setLong(2,fromID);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int rs1 = 0;
		try {


			rs1 = prest.executeUpdate();
			long elapsedTime = System.currentTimeMillis() - start;

			totalAddEntryElapsed += elapsedTime;

			//writeToFile( String.valueOf(elapsedTime),"InsertMySQL");
			//user.id = userID;

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	private static void createTable(Long fromID) {

		Statement s = null;

		try {
			s = conn.createStatement ();
		} catch (SQLException e) {

			e.printStackTrace();
		}


		String tablename = "user" + fromID.toString();

		String tableQuery = "CREATE TABLE "+ tablename + " ("
		+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
		+ "PRIMARY KEY (id),"
		+ "to_fk CHAR(20), from_fk CHAR(20))" ;

		try {
			s.executeUpdate ("DROP TABLE IF EXISTS user"+ fromID.toString());
			s.executeUpdate (

					tableQuery		
			);


		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.close ();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Boolean tableExists(long id) {

		// our SQL SELECT query. 
		// if you only need a few columns, specify them by name instead of using "*"
		String query = "SELECT table_name FROM information_schema.tables WHERE table_name = 'user" + id + "'";


		// create the java statement
		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// execute the query, and get a java resultset
		ResultSet rs = null;
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Boolean t = false;

		try {
			t = rs.first();
			st.close ();
		} catch (SQLException e) {
			e.printStackTrace();
		}		

		return t;


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

	private static void createNodes() {


		int i = 0;

		while(i++ < total)
		{

			addNode(i);

		}

		System.out.println (" added all nodes");

	}

	static void createTables(){

		Statement s = null;

		try {
			s = conn.createStatement ();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.executeUpdate ("DROP TABLE IF EXISTS user");
			s.executeUpdate (
					"CREATE TABLE user ("
					+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
					+ "PRIMARY KEY (id),"
					+ "nickname CHAR(20), password CHAR(20), email CHAR(20))");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.close ();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (schema.equals("one"))
			createFrienshipTable();

	}
	private static void createFrienshipTable() {

		Statement s = null;

		try {
			s = conn.createStatement ();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.executeUpdate ("DROP TABLE IF EXISTS friendship");
			s.executeUpdate (
					"CREATE TABLE friendship ("
					+ "id INT UNSIGNED NOT NULL AUTO_INCREMENT,"
					+ "PRIMARY KEY (id),"
					+ "to_ID INT, from_ID INT)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			s.close ();
		} catch (SQLException e) {
			e.printStackTrace();
		}



	}

	private static void addNode(int i) {

		int count = 0;
		String sql =         "INSERT INTO user (nickname,password,email)"
			+ " VALUES (?,?,?)";
		long start = System.currentTimeMillis();  
		PreparedStatement prest=null;		
		try {

			prest = conn.prepareStatement(sql);
			prest.setString(1,ASCIIString(20));
			prest.setString(2,ASCIIString(20));
			prest.setString(3,ASCIIString(20));

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int rs1 = 0;
		try {
			rs1 = prest.executeUpdate();

			long elapsedTime = System.currentTimeMillis() - start;

			totalAddEntryElapsed += elapsedTime;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
