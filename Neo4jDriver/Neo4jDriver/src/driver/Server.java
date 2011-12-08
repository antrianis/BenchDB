package driver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public ServerSocket serverSocket = null;

	public static void main(String[] args) {

		Server server = new Server();
		Driver neoDB = new Driver();

		try {

			server.serverSocket = new ServerSocket(General.ServerPort);

		} catch (IOException e) {
			System.out
			.println("Could not listen on port: " + General.ServerPort); //$NON-NLS-1$
			System.exit(-1);
		}

		System.out.println("Neo4j Server Running...");

		while (true) {

			Socket clientSocket = null;

			try {
				clientSocket = server.serverSocket.accept();

			} catch (IOException e) {

				System.out.println("Accept failed: " + General.ServerPort); //$NON-NLS-1$
				System.exit(-1);
			}

			ClientCommunicator cc = new ClientCommunicator(clientSocket, neoDB);

			Thread t = new Thread(cc);
			t.start();

		}
	}
}
