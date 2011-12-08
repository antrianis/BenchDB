package driver;

public class General {

	public static final int ServerPort = 44441;
	public static final int DEBUG = 1; // DEBUG_LEVEL 0,1,2

	public enum Code {
		OK, ERROR
	}

	public enum NodeType {

		notice, user, NOTICES, profile, SUBSCRIPTIONS
	}

	public enum EdgeType {

		FOLLOW_REL, OWNS_MESSAGE_REL
	}

}
