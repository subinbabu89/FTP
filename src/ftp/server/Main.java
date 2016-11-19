/**
 * 
 */
package ftp.server;

import java.io.IOException;

import ftp.utility.Constants;

public class Main {

	public static void main(String[] args) {

		ServerPI serverPI = new ServerPI(Constants.SERVER_CONTROL_PORT);
		
		try {
			serverPI.openConnection();

			serverPI.manageRequestResponse();

			serverPI.closeConnection();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
