package databases;

import java.util.HashMap;
import java.util.Vector;

public interface DB {

	public abstract void initialize();

	public abstract void closeConnection();

	public abstract boolean delete(int in);

	public abstract HashMap<String, String> readNode(int in);

	public abstract boolean update(int in);

	public abstract Vector<HashMap<String, String>> getFollowers(int id);

	public abstract Vector<HashMap<String, String>> getFollowersOnlyIds(int id);

	public abstract void degreeOfSeparation(int startNode, int endNode,
			int maxPathLenght);

	public abstract void getTotalTime(int totalOps);

	public abstract void addNode();

	public abstract void addEdge(int in1, int in2);

	public abstract void removeNode(int in1);

	public abstract void removeEdge(int in1, int in2);

}
