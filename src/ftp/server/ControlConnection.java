package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import ftp.commands.PASV;

public class ControlConnection implements Runnable{
	
	private DataOutputStream dataoutputstream = null;
	private DataInputStream datainputstream = null;
	
	private int server_data_port;
	
	Socket clientSocket;
	
	public ControlConnection(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	public void acceptClientRequest() throws IOException {
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
			} 
			if(command.contains("PORT")){
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
	
	public void sendServerResponse() throws IOException {
		dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());
		
		dataoutputstream.writeUTF(Integer.toString(server_data_port));
	}

	@Override
	public void run() {
		try {
			while(true){
				acceptClientRequest();
				
				sendServerResponse();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	
	
	// Creating the threads for data connections
}
