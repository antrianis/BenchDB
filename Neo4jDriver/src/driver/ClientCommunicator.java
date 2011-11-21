package driver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * The Class ClientCommunicator.
 */
public class ClientCommunicator implements Runnable {

	/** The socket. */
	private Socket socket;

	/** The br. */
	private BufferedReader br;

	/** The wr. */
	private BufferedWriter wr;

	/** The neo db. */
	private Driver neoDB;

	/**
	 * Instantiates a new client communicator.
	 * 
	 * @param server
	 *            the server
	 * @param db
	 *            the db
	 */
	public ClientCommunicator(Socket server, Driver db) {
		this.socket = server;
		this.neoDB = db;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

			try {

				try {
					this.br = new BufferedReader(new InputStreamReader(this.socket
							.getInputStream())); // get the client input stream
					this.wr = new BufferedWriter(new OutputStreamWriter(this.socket
							.getOutputStream()));

					String jsonPacket = this.br.readLine(); // read JSON packet from client
					String result = this.neoDB.handleRequest(jsonPacket);
					if (result == null
							|| result.equals(General.Code.ERROR.toString()))
						this.wr.write("ERROR");
					else
						this.wr.write(result);

					this.wr.flush();

				} catch (Throwable e) {

					this.wr.write(General.Code.ERROR.toString());
					this.wr.flush();

					System.out.println("Error " + e.getMessage()); //$NON-NLS-1$
					e.printStackTrace();
				}

				//this.wr.close(); // close the client output stream
				//this.br.close(); // close the client input stream
				//this.socket.close();

			} catch (IOException ioe) {

				System.out.println("IOException on socket listen: " + ioe); //$NON-NLS-1$
				ioe.printStackTrace();
			}
		
	}
}
