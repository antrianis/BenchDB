package driver;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Driver {

	GraphDatabaseService graphDb;
	// IndexManager index;
	// private Index<Node> userNicknamesIndex;
	private Implementation neo4jAPI;


	public Driver() {

		System.out.println(System.getProperty("user.dir"));;
		this.graphDb = new EmbeddedGraphDatabase("db"); //$NON-NLS-1$

		this.neo4jAPI = new Implementation();

		//		this.neo4jAPI.printAllGraphNodes(graphDb);

		this.neo4jAPI.initialize(this.graphDb);

		// if (General.DEBUG == 1)
		// TestFunctions.printAllGraphNodes(this.graphDb);
		// shutDownNeo4j();
	}

	public General.Code shutDownNeo4j() {

		this.graphDb.shutdown();
		return General.Code.OK;

	}

	public String handleRequest(String jsonPacket) {

		Gson gson = new Gson();
		final JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonPacket).getAsJsonArray();
		int function_code = gson.fromJson(array.get(0), Integer.class);

		switch (function_code) {

		case 0: {

			// this.neo4jAPI.delete(i.next().getValue().getAsInt());
			System.out.println("DUMP");

			break;
		}
		case 1: { /* Read Node */
			this.neo4jAPI.readNode(gson.fromJson(array.get(1), int.class));
			break;
		}
		case 2: {  /* Add Node */
			this.neo4jAPI.update(gson.fromJson(array.get(1),int.class));
			break;
		}
		case 3: { /* Add Edge */
			this.neo4jAPI.addEdge(gson.fromJson(array.get(1),
					int.class), gson.fromJson(array.get(2), int.class));
			//this.neo4jAPI.getFollowers(gson.fromJson(array.get(1), int.class));
			break;
		}
		case 4: { /* Get Followers Only Ids */

			this.neo4jAPI.getFollowersOnlyIds(gson.fromJson(array.get(1),
					int.class));
			break;
		}
		case 5: {

			this.neo4jAPI.degreeOfSeparation(gson.fromJson(array.get(1),
					int.class), gson.fromJson(array.get(2), int.class), gson
					.fromJson(array.get(3), int.class));
			break;
			// this.neo4jAPI.warmCache(this.graphDb);
		}
		case 6: { /* Add Node */

			this.neo4jAPI.addNode();
			break;
		}
		case 7: { /* Add Node */

			this.neo4jAPI.removeEdge(gson.fromJson(array.get(1),
					int.class), gson.fromJson(array.get(2), int.class));
			break;
		}
		case 8: { /* Add Node */

			this.neo4jAPI.removeNode(gson.fromJson(array.get(1), int.class));
			break;
		}
		}

		return jsonPacket;

	}

}
