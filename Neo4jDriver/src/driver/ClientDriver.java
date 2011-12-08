package driver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;

import com.google.gson.Gson;

public class ClientDriver {
	String address = "localhost";
	int port = 44442;
	private Socket socket;
	private BufferedWriter wr;
	Gson gson = new Gson();

	public void connect() {

		try {

			socket = new Socket(this.address, this.port);
			wr = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream()));
		} catch (UnknownHostException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void executeQuery(Collection<?> a) {

		//connect();
		try {
			wr.write(gson.toJson(a));
			wr.write("\n");
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//closeConnection();
	}

	public void closeConnection() {
       try {
		socket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
