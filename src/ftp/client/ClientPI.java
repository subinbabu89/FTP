/**
 * 
 */
package ftp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ftp.utility.Constants;

/**
 * @author Sahana Ravikumar
 *
 */

public class Client {

	private static Socket clientSocket;

	private static DataOutputStream dataoutputstream = null;

	private static DataInputStream datainputstream = null;

	public static void main(String[] args) {
		
		try {
			clientSocket = new Socket(Constants.SERVER_IP_ADDRESS, Constants.SERVER_PORT);

			datainputstream = new DataInputStream(clientSocket.getInputStream());
			dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());

			System.out.println("Client saying hello");
			dataoutputstream.writeUTF("hello");

			System.out.println("Client waiting...");
			String responseFromServer = datainputstream.readUTF();
			System.out.println("The server response is " + responseFromServer);

			clientSocket.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}