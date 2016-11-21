/**
 * 
 */
package ftp.client;

import java.io.IOException;

import ftp.utility.Constants;

public class Main {

	public static void main(String[] args) {

		int serverDataPort;
		
		ClientPI clientPI = new ClientPI(Constants.SERVER_IP_ADDRESS, Constants.SERVER_CONTROL_PORT);
		
		try {
			clientPI.openConnection();

			clientPI.sendClientRequest("PASV");

			serverDataPort = clientPI.receiveServerResponse();
			
			System.out.println("Main : " + serverDataPort);
			
			clientPI.sendClientRequest("PORT " + Constants.SERVER_IP_ADDRESS + " " + serverDataPort);

			clientPI.receiveServerResponse();
			
			clientPI.sendClientRequest("QUIT");
			
			//clientPI.closeConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
