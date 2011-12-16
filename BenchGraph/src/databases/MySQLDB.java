package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class MySQLDB implements DB {

	static Connection conn = null;
	static Random random = new Random();
	private double totalTime = 0;

	/* (non-Javadoc)
	 * @see databases.DB2#initialize()
	 */
	public void initialize() {

		String userName = "socialUser";
		String password = "socialUser";
		String url = "jdbc:mysql://localhost/social";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see databases.DB2#degreeOfSeparation(int, int, int)
	 */
	public void degreeOfSeparation(int startNode, int endNode, int maxPathLenght) {

		long start = System.currentTimeMillis();

		String sql = "SELECT  d2.to_ID  " +
		"FROM friendship d1 JOIN friendship d2 ON d1.from_ID = d2.to_ID " +
		"AND (d2.from_ID = "+ startNode + " AND d1.to_ID = " + endNode +  "|| " +
		"("
		+
		"(d1.to_ID = "+ startNode + " AND d2.from_ID = " + endNode + ") " + "||" +
		"(d1.to_ID = "+ endNode + " AND d2.from_ID = " + startNode + ") " +
		")" + 
		")" +
		" UNION " +
		"SELECT  d2.from_ID  " +
		"FROM friendship d1 JOIN friendship d2 ON d1.from_ID = d2.from_ID " +
		"AND (" +
		"(d2.to_ID = "+ startNode + " AND d1.to_ID = " + endNode + ") " + "||" +
		"(d2.to_ID = "+ endNode + " AND d1.to_ID = " + startNode + ") " +
		")" +
		" UNION " +
		"SELECT  d2.to_ID  " +
		"FROM friendship d1 JOIN friendship d2 ON d1.to_ID = d2.to_ID " +
		"AND (" +
		"(d1.from_ID = "+ startNode + " AND d2.from_ID = " + endNode + ") " + "||" +
		"(d1.from_ID = "+ endNode + " AND d2.from_ID = " + startNode + ") " +
		")" 
		;


		Statement s = null;
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet executeQuery = null;
		try {
			executeQuery = s.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//		try {
		//			while (executeQuery.next()) {
		//
		//				System.out.println(executeQuery.getInt(1));
		//
		//			}
		//		} catch (SQLException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}
		try {
			executeQuery.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
	}

	/* (non-Javadoc)
	 * @see databases.DB2#getFollowersManyTables(int)
	 */
	public Vector<HashMap<String, String>> getFollowersManyTables(int id)
	throws SQLException {

		Vector<HashMap<String, String>> v = new Vector<HashMap<String, String>>();
		Statement s = null;
		long start = System.currentTimeMillis();
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String tableName = "user" + id;
		String sql = "SELECT from_fk FROM " + tableName;

		s.executeQuery(

				sql);
		// For every follower go-to initial table and get it

		ResultSet rs = s.getResultSet();

		while (rs.next()) {

			v.add(readNodeNoTime(rs.getInt("from_fk")));

		}
		try {
			rs.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return v;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#getFollowersOneTable(int)
	 */
	public Vector<HashMap<String, String>> getFollowersOneTable(int id)
	throws SQLException {

		Vector<HashMap<String, String>> v = new Vector<HashMap<String, String>>();
		Statement s = null;
		long start = System.currentTimeMillis();
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// String tableName = "friendship";
		String sql = "SELECT from_ID FROM friendship WHERE to_ID ='" + id + "'";

		s.executeQuery(

				sql);
		// For every follower go-to initial table and get it
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			v.add(readNodeNoTime(rs.getInt("from_ID")));
		}
		try {
			rs.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return v;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#getFollowersOneTableOnlyIds(int)
	 */
	public Vector<HashMap<String, String>> getFollowersOneTableOnlyIds(int id)
	throws SQLException {

		Vector<HashMap<String, String>> v = new Vector<HashMap<String, String>>();

		Statement s = null;
		long start = System.currentTimeMillis();
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// String tableName = "friendship";
		String sql = "SELECT from_ID FROM friendship WHERE to_ID ='" + id + "'";

		s.executeQuery(

				sql);

		// For every follower go-to initial table and get it
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("id", String.valueOf(rs.getInt("from_ID")));
			v.add(hm);
		}
		try {
			rs.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return v;

	}

	private static Boolean tableExists(long id) {

		String query = "SELECT table_name FROM information_schema.tables WHERE table_name = 'user"
			+ id + "'";

		Statement st = null;
		try {
			st = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet rs = null;
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			return rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#getFollowersManyOnlyIds(int)
	 */
	public Vector<HashMap<String, String>> getFollowersManyOnlyIds(int id)
	throws SQLException {

		Vector<HashMap<String, String>> v = new Vector<HashMap<String, String>>();
		Statement s = null;

		long start = System.currentTimeMillis();

		if (tableExists(id)) {

			try {
				s = conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			String tableName = "user" + id;
			String sql = "SELECT from_fk FROM " + tableName;

			s.executeQuery(

					sql);

			// For every follower go-to initial table and get it
			HashMap<String, String> hm = new HashMap<String, String>();
			ResultSet rs = s.getResultSet();

			while (rs.next()) {

				hm.put("id", rs.getString(1)); // getString ("from_fk")


			}
			try {
				rs.close();
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			long elapsedTime = System.currentTimeMillis() - start;
			totalTime += elapsedTime;
		}
		return v;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#delete(int)
	 */
	public boolean delete(int in) {

		long start = System.currentTimeMillis();
		Statement s = null;
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = "DELETE FROM user WHERE id =" + in;

		try {
			s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
		return true;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#readNodeNoTime(int)
	 */
	public HashMap<String, String> readNodeNoTime(int in) throws SQLException {

		Statement s = null;
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = "SELECT * FROM user WHERE id =" + in;

		s.executeQuery(

				sql);
		HashMap<String, String> hm = null;
		ResultSet rs = s.getResultSet();

		while (rs.next()) {

			hm = new HashMap<String, String>();
			hm.put("nickname", rs.getString("nickname"));
			hm.put("password", rs.getString("password"));
			hm.put("email", rs.getString("email"));

		}
		try {
			rs.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hm;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#readNode(int)
	 */
	public HashMap<String, String> readNode(int in) {

		Statement s = null;
		long start = System.currentTimeMillis();
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sql = "SELECT * FROM user WHERE id =" + in;

		try {
			s.executeQuery(

					sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		HashMap<String, String> hm = null;
		ResultSet rs = null;
		try {
			rs = s.getResultSet();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			while (rs.next()) {

				hm = new HashMap<String, String>();
				hm.put("nickname", rs.getString("nickname"));
				hm.put("password", rs.getString("password"));
				hm.put("email", rs.getString("email"));

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			rs.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

		return hm;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#update(int)
	 */
	public boolean update(int in) {
		long start = System.currentTimeMillis();
		try {

			String query = "update user set nickname = ? ,password = ?,email = ? where id = "
				+ in;
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, ASCIIString(20));
			preparedStmt.setString(2, ASCIIString(20));
			preparedStmt.setString(3, ASCIIString(20));

			// execute the java preparedstatement
			preparedStmt.executeUpdate();

		} catch (Exception e) {

			System.err.println(e.getMessage());
		}

		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
		return true;

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

	/* (non-Javadoc)
	 * @see databases.DB2#getTotalTime(int)
	 */
	public void getTotalTime(int totalOps) {

		System.out.println(String.valueOf((totalTime * 100.0) / totalOps));
	}

	/* (non-Javadoc)
	 * @see databases.DB2#getFollowers(int)
	 */
	public Vector<HashMap<String, String>> getFollowers(int id) {

		try {
			getFollowersOneTableOnlyIds(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// getFollowersOneTable(id);
		// getFollowersManyTables(id);
		return null;
	}

	/* (non-Javadoc)
	 * @see databases.DB2#closeConnection()
	 */
	public void closeConnection() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see databases.DB2#addNode()
	 */
	public void addNode() {

		String sql =  "INSERT INTO user (nickname,password,email)" + " VALUES (?,?,?)";
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
			totalTime += elapsedTime;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see databases.DB2#addEdge(long, long)
	 */
	public void addEdge(int toID, int fromID) {
		long start = System.currentTimeMillis();  

		int count = 0;
		String tableName = "friendship";
		String sql =  "INSERT INTO "+ tableName + " (to_ID,from_ID)"
		+ " VALUES (?,?)"; //$NON-NLS-1$

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
			e1.printStackTrace();
		}

		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
	}

	/* (non-Javadoc)
	 * @see databases.DB2#removeNode(int)
	 */
	public void removeNode(int in1) {
		
		long start = System.currentTimeMillis();  

		int count = 0;
		String tableName = "user";
		String sql =  "DELETE FROM "+ tableName + " WHERE (id)"
		+ " VALUES (?)"; //$NON-NLS-1$
		PreparedStatement prest=null;		
		try {

			prest = conn.prepareStatement(sql);
			prest.setLong(1,in1);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int rs1 = 0;
		try {


			rs1 = prest.executeUpdate();


		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;

	}

	/* (non-Javadoc)
	 * @see databases.DB2#removeEdge(int, int)
	 */
	public void removeEdge(int in1, int in2) {

	
		long start = System.currentTimeMillis();  

		int count = 0;
		String tableName = "friendship";
		String sql =  "DELETE FROM "+ tableName + " WHERE (to_ID,from_ID)"
		+ " VALUES (?,?)"; //$NON-NLS-1$
		PreparedStatement prest=null;		
		try {

			prest = conn.prepareStatement(sql);
			prest.setLong(1,in1);
			prest.setLong(1,in2);

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		int rs1 = 0;
		try {


			rs1 = prest.executeUpdate();


		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			prest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		totalTime += elapsedTime;
	}


	public Vector<HashMap<String, String>> getFollowersOnlyIds(int id) {
		System.out.println("DUMP");
		return null;
	}


}


