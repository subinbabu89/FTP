package ftp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		ServerSocket serverSocket;
		ServerSocket telnetSocket;

		try {
			serverSocket = new ServerSocket(21);

			telnetSocket = new ServerSocket(23);
			
			while (true) {
				System.out.println("Server listening on port 21");
				Socket request_socket = serverSocket.accept();
				
				System.out.println("Telnet server listening on port 23");
				Socket telnetClientSocket = telnetSocket.accept();
				
				Runnable controlConnection = new ControlConnection(request_socket);
				new Thread(controlConnection).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
