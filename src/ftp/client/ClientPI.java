/**
 * 
 */
package ftp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Sahana Ravikumar
 *
 */
public class ClientPI {

	private static Socket clientSocket;
	private static DataOutputStream dataoutputstream = null;
	private static DataInputStream datainputstream = null;

	private String server_ip;
	private int server_port;
//
	public ClientPI(String server_ip, int server_port) {
		this.server_ip = server_ip;
		this.server_port = server_port;
	}

	public void openConnection() throws UnknownHostException, IOException {
		clientSocket = new Socket(server_ip, server_port);
		datainputstream = new DataInputStream(clientSocket.getInputStream());
		dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void sendClientRequest() throws IOException {
		System.out.println("Client saying hello");
		dataoutputstream.writeUTF("hello");
	}

	public void receiveServerResponse() throws IOException {
		System.out.println("Client waiting...");
		String responseFromServer = datainputstream.readUTF();
		System.out.println("The server response is " + responseFromServer);
	}

	public void closeConnection() throws IOException {
		clientSocket.close();
	}
}