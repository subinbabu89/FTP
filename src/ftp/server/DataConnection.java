package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class DataConnection implements Runnable {

	private int server_data_port;
	private String command;
	private Socket req_socket;
	private String username;

	public DataConnection(String username, String command, Socket req_socket) {
		this.req_socket = req_socket;
		this.command = command;
		this.username = username;
	}

	@Override
	public void run() {

		System.out.println("In run method");
		if (command.equals("STOR")) {
			System.out.println("In server save file");
			receiveFile();
		} else if (command.equals("RETR")) {
			System.out.println("In server send file");
			sendFile();
		}
	}

	private String getFileName(String command2) {
		StringTokenizer tokens = new StringTokenizer(command, " ");
		tokens.nextToken();
		return tokens.nextToken();
	}

	private void receiveFile() {
		DataInputStream din;

		try {
			din = new DataInputStream(req_socket.getInputStream());
			String filename = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/server/sampleServer.txt";

			File f = new File(filename);

			FileOutputStream fout = new FileOutputStream(f);
			int ch;
			String temp;
			do {
				temp = din.readUTF();
				ch = Integer.parseInt(temp);
				if (ch != -1) {
					fout.write(ch);
				}
			} while (ch != -1);
			fout.close();
			System.out.println("server: file received successfully");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendFile() {
		DataOutputStream dout;

		try {
			dout = new DataOutputStream(req_socket.getOutputStream());

			String filename = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/server/download_server.txt";
			File f = new File(filename);
			FileInputStream fin = new FileInputStream(f);
			int ch;
			do {
				ch = fin.read();
				dout.writeUTF(String.valueOf(ch));
			} while (ch != -1);
			fin.close();
			System.out.println("Server: File Sending done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		;

	}
}
