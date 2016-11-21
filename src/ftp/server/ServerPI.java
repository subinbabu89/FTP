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
import java.util.StringTokenizer;

import ftp.commands.PASV;
import ftp.commands.PORTCommand;
import ftp.connections.ServerDTP;

public class ServerPI{

	private static ServerSocket serverSocket;
	private int server_port;	
	
	public ServerPI(){
		
	}
	
	public ServerPI(int serverPort) {
		this.server_port = serverPort;
	}

	public void openConnection() throws UnknownHostException, IOException {
		serverSocket = new ServerSocket(server_port);
	}

	public void manageRequestResponse() throws IOException {
		
		
		while (true) {

			Socket requestSocket = serverSocket.accept();
			ControlConnection connObject = new ControlConnection(requestSocket);
			Thread t = new Thread(connObject);
			t.start();
			
			//closeRequestSocket(requestSocket); //figure out later
		}
	}

	public void closeRequestSocket(Socket clientSocket) throws IOException {
		clientSocket.close();
	}

	public void closeConnection() throws IOException {
		serverSocket.close();
	}
}
