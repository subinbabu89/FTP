package ftp.connections;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerDTP {
	
	private String host;
	private int dataport;
	
	public ServerDTP(String host, int dataport){
		this.host = host;
		this.dataport = dataport;
	}
	
	public void openDataConnection() throws UnknownHostException, IOException{
		Socket socket = new Socket(host, dataport);
		
		
	}
}
