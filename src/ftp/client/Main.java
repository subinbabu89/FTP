/**
 * 
 */
package ftp.client;

import java.io.IOException;

import ftp.utility.Constants;

/**
 * @author Sahana Ravikumar
 *
 */
public class Main {

	public static void main(String[] args) {

		ClientPI clientPI = new ClientPI(Constants.SERVER_IP_ADDRESS, Constants.SERVER_PORT);
		
		try {
			clientPI.openConnection();

			clientPI.sendClientRequest();

			clientPI.receiveServerResponse();

			clientPI.closeConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
