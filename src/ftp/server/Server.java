/**
 * 
 */
package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ftp.utility.Constants;

/**
 * @author Sahana Ravikumar
 *
 */

public class Server {

	private static ServerSocket serverSocket;

	private static DataOutputStream dataoutputstream = null;

	private static DataInputStream datainputstream = null;

	public static void main(String[] args) {

		try {
			serverSocket = new ServerSocket(Constants.SERVER_PORT);

			while (true) {

				Socket requestSocket = serverSocket.accept();

				datainputstream = new DataInputStream(requestSocket.getInputStream());
				dataoutputstream = new DataOutputStream(requestSocket.getOutputStream());

				System.out.println("Reading...");
				String sentence = datainputstream.readUTF();
				System.out.println("The sentence read in server is " + sentence);

				dataoutputstream.writeUTF(sentence);

				requestSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
