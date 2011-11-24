package databases;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public interface  DB {


	boolean delete(int in);
	boolean update(int in);
	
	HashMap<String,String> readNode(int in) throws SQLException;
	Vector<HashMap<String, String>> getFollowers(int id) throws SQLException;
	void degreeOfSeparation(int startNode, int endNode , int maxPathLenght);


}
