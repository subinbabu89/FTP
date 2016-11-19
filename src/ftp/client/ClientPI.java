/**
 * 
 */
package ftp.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientPI {

	private static Socket clientSocket;
	private static DataOutputStream dataoutputstream = null;
	private static DataInputStream datainputstream = null;

	private String server_ip;
	private int server_port;

	public ClientPI(String server_ip, int server_port) {
		this.server_ip = server_ip;
		this.server_port = server_port;
	}

	public void openConnection() throws UnknownHostException, IOException {
		clientSocket = new Socket(server_ip, server_port);
	}

	public void sendClientRequest(String command) throws IOException {
		datainputstream = new DataInputStream(clientSocket.getInputStream());
		dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());
		System.out.println("Client: Sending the command " + command);
		dataoutputstream.writeUTF(command);
	}

	public int receiveServerResponse() throws IOException {
		System.out.println("Client waiting...");
		
		int responseFromServer = Integer.parseInt(datainputstream.readUTF());
		System.out.println("The server response is " + responseFromServer);
		return responseFromServer;
	}

	public void closeConnection() throws IOException {
		clientSocket.close();
	}
}