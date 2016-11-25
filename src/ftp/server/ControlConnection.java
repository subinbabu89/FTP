package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

public class ControlConnection implements Runnable {

	Socket request_socket;

	private DataOutputStream dataoutputstream = null;
	private DataInputStream datainputstream = null;

	public ControlConnection(Socket request_socket) throws IOException {
		this.request_socket = request_socket;
	}

	@Override
	public void run() {
		int data_port = 0;
		Socket requ_socket=null;
		
		try {
			while (true) {

				datainputstream = new DataInputStream(request_socket.getInputStream());
				dataoutputstream = new DataOutputStream(request_socket.getOutputStream());

				String command = datainputstream.readUTF();
				String cmd = null;
				
				System.out.println("the cmd read is " + command);

				if(command.equals("PASV"))
					cmd = "PASV";
				else if (command.contains("STOR")) {
					System.out.println("stor received");
					cmd = "STOR";
				} else if (command.contains("USER")) {
					System.out.println("USER cmd received");
					cmd = "USER";
				}else if(command.equals("QUIT"))
					cmd = "QUIT";
				
				switch (cmd) {

				case "PASV":
					data_port = generateDataPort();
					System.out.println("The data port is " + data_port);
					dataoutputstream.writeUTF(String.valueOf(data_port));
					ServerSocket serverSocket = new ServerSocket(data_port);
					System.out.println("Server listening on port " + data_port);
					requ_socket = serverSocket.accept();
					break;

				case "USER":
					String username = checkUser(command);
					listFiles(username);
					break;

				case "STOR": {
					System.out.println("In store");
					Runnable dataConnection = new DataConnection(requ_socket, "store");
					new Thread(dataConnection).start();
					break;
				}

				case "RETR": {
					System.out.println("In store");
					Runnable dataConnection = new DataConnection(requ_socket, "retr");
					new Thread(dataConnection).start();
					break;
				}

				case "QUIT": {
						System.out.println("quit command received");
						return;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				request_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String checkUser(String command) {
		StringTokenizer tokens = new StringTokenizer(command, " ");
		tokens.nextToken();
		String username = tokens.nextToken();
		File file = new File(username);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("User Directory created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			System.out.println("User Directory exists");
		}
		return username;
	}

	private void listFiles(String filePath) {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		if(listOfFiles.length > 0)
			System.out.println("The files present are");
		for (File file : listOfFiles) {
			if (file.isFile()) {
				System.out.println(file.getName());
			}
		}
	}

	private int generateDataPort() {
		int data_port;
		Random randomNumber;

		do {
			randomNumber = new Random();
			data_port = randomNumber.nextInt((65535 - 1023) + 1);
		} while (data_port < 1023);

		return data_port;
	}

}
