/**
 * 
 */
package ftp.commands;

import java.util.Random;

import ftp.utility.Constants;

public class PASV {

	private int port;
	
	/*public int getSuccessCode(){
		return Constants.PASV_SUCCESS_CODE;
	}
	
	public int getErrorCode(){
		return Constants.PASV_ERROR_CODE;
	}*/
	
	public int getPort(){
		Random randomNumber;
			
		/*
		 * port should be between 1023 and 65535
		 */
		do{
			randomNumber = new Random();
			port = randomNumber.nextInt((Constants.MAXIMUM_PORT_NUMBER - Constants.MINIMUM_PORT_NUMBER) + 1);
		}while(port < Constants.MINIMUM_PORT_NUMBER);
		
		return port;
	}
}
