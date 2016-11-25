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
		try {
			while (true) {

				datainputstream = new DataInputStream(request_socket.getInputStream());
				dataoutputstream = new DataOutputStream(request_socket.getOutputStream());

				String command = datainputstream.readUTF();

				System.out.println("the cmd read is " + command);

				if (command.contains("STOR")) {
					System.out.println("stor received");
					savefile();
				} else if (command.contains("USER")) {
					System.out.println("USER cmd received");
					checkUser(command);
				}

				switch (command) {

				case "PASV":
					int data_port = generateDataPort();
					System.out.println("The data port is " + data_port);
					dataoutputstream.writeUTF(String.valueOf(data_port));
					break;

				case "list":
					listFiles();
					break;

				case "STOR": {
					System.out.println("In store");
					savefile();
					ServerSocket serverSocket = new ServerSocket(1025);
					Socket req_socket = serverSocket.accept();
					Runnable dataConnection = new DataConnection(req_socket, "store");
					new Thread(dataConnection).start();
					break;
				}

				case "retr": {
					System.out.println("In store");
					ServerSocket serverSocket = new ServerSocket(1029);
					Socket req_socket = serverSocket.accept();
					Runnable dataConnection = new DataConnection(req_socket, "retr");
					new Thread(dataConnection).start();
					break;
				}

				case "quit": {
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

	private void checkUser(String command) {
		StringTokenizer tokens = new StringTokenizer(command, " ");
		tokens.nextToken();
		String username = tokens.nextToken();
		File file = new File(username);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			System.out.println("Directory exists");
		}
	}

	private void savefile() {
		/*
		 * InputStream in = null; OutputStream out = null;
		 * 
		 * try { in = request_socket.getInputStream(); out = new
		 * FileOutputStream("D:/SAHANA/ab.txt"); byte[] bytes = new
		 * byte[16*1024];
		 * 
		 * int count; while ((count = in.read(bytes)) > 0) { out.write(bytes, 0,
		 * count); } } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	private void listFiles() {
		File folder = new File("D:/SAHANA/docs");
		File[] listOfFiles = folder.listFiles();

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
