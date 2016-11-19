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
		
		ClientPI clientPI1 = new ClientPI(Constants.SERVER_IP_ADDRESS, Constants.SERVER_CONTROL_PORT);
		
		try {
			clientPI.openConnection();

			clientPI.sendClientRequest("PASV");

			serverDataPort = clientPI.receiveServerResponse();
			
			System.out.println("Main : " + serverDataPort);
			
			clientPI.closeConnection();
			
			clientPI1.openConnection();
			
			clientPI1.sendClientRequest("PORT " + Constants.SERVER_IP_ADDRESS + " " + serverDataPort);

			clientPI1.receiveServerResponse();
			
			clientPI1.closeConnection();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
