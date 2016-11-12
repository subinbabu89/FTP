/**
 * 
 */
package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sahana Ravikumar
 *
 */
public class ServerPI {

	private static ServerSocket serverSocket;
	private static DataOutputStream dataoutputstream = null;
	private static DataInputStream datainputstream = null;

	private int server_port;

	public ServerPI(int serverPort) {
		this.server_port = serverPort;
	}

	public void openConnection() throws UnknownHostException, IOException {
		serverSocket = new ServerSocket(server_port);
	}

	public void manageRequestResponse() throws IOException {
		while (true) {

			Socket requestSocket = serverSocket.accept();

			acceptClientRequest(requestSocket);

			sendServerResponse(requestSocket);

			closeRequestSocket(requestSocket);
		}
	}

	public void acceptClientRequest(Socket clientSocket) throws IOException {
		datainputstream = new DataInputStream(clientSocket.getInputStream());
		System.out.println("Reading...");
		String sentence = datainputstream.readUTF();
		System.out.println("The sentence read in server is " + sentence);
	}

	public void sendServerResponse(Socket clientSocket) throws IOException {
		dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());
		String response = "Hello from Server!!";
		dataoutputstream.writeUTF(response);
	}

	public void closeRequestSocket(Socket clientSocket) throws IOException {
		clientSocket.close();
	}

	public void closeConnection() throws IOException {
		serverSocket.close();
	}
}
