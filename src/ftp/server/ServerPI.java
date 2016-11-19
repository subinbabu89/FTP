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

public class ServerPI {

	private static ServerSocket serverSocket;
	private static DataOutputStream dataoutputstream = null;
	private static DataInputStream datainputstream = null;

	private int server_port;	
	private int server_data_port;

	public ServerPI(int serverPort) {
		this.server_port = serverPort;
	}

	public void openConnection() throws UnknownHostException, IOException {
		serverSocket = new ServerSocket(server_port);
	}

	public void manageRequestResponse() throws IOException {
		while (true) {

			Socket requestSocket = serverSocket.accept();

			acceptClientRequest(requestSocket);

			sendServerResponse(requestSocket);

			//closeRequestSocket(requestSocket); //figure out later
		}
	}

	public void acceptClientRequest(Socket clientSocket) throws IOException {
		String ipaddr = null;
		int data_port = 0;
		
		datainputstream = new DataInputStream(clientSocket.getInputStream());
		System.out.println("Reading...");
		String command = datainputstream.readUTF();
		System.out.println("The command read in server is " + command);
		
		if(command.equals("PASV")){
			PASV pasvObject  = new PASV();
			server_data_port = pasvObject.getPort();
			System.out.println("The port on which the server is listening for data connection : " + server_data_port);
		}else if(command.contains("PORT")){
			System.out.println("in port cmd");
			StringTokenizer tokens = new StringTokenizer(command, " ");
			System.out.println("The tokens are " + tokens.nextToken());
			
			while(tokens.hasMoreTokens()){
				ipaddr = tokens.nextToken();
				data_port = Integer.parseInt(tokens.nextToken());
			}
			
			/*ServerDTP serverdtp_object = new ServerDTP(ipaddr, data_port);
			serverdtp_object.openDataConnection();
			*/
			//PORTCommand portObject = new PORTCommand(tokens.nextToken().);
			
		}
	}

	public void sendServerResponse(Socket clientSocket) throws IOException {
		dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());
		
		dataoutputstream.writeUTF(Integer.toString(server_data_port));
	}

	public void closeRequestSocket(Socket clientSocket) throws IOException {
		clientSocket.close();
	}

	public void closeConnection() throws IOException {
		serverSocket.close();
	}
}
