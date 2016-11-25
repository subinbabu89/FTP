package ftp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		ServerSocket serverSocket;

		try {
			serverSocket = new ServerSocket(21);

			while (true) {
				Socket request_socket = serverSocket.accept();
				Runnable controlConnection = new ControlConnection(request_socket);
				new Thread(controlConnection).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
