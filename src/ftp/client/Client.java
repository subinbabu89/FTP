package ftp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private static Socket clientSocket;

	public static void main(String[] args) {

		DataOutputStream dataoutputstream = null;
		DataInputStream datainputstream = null;

		try {
			Scanner scanner = new Scanner(System.in);

			System.out.println("Enter the host to connect: ");
			String hostName = scanner.nextLine();

			System.out.println("Enter the username: ");
			String username = scanner.nextLine();

			InetAddress host = InetAddress.getByName(hostName);
			clientSocket = new Socket(host, 21);

			System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + " for Control Connection");

			datainputstream = new DataInputStream(clientSocket.getInputStream());
			dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());

			dataoutputstream.writeUTF("USER " + username);

			dataoutputstream.writeUTF("PASV");

			int data_port = Integer.parseInt(datainputstream.readUTF());

			System.out.println("The data port received at the client side is " + data_port);

			// dataoutputstream.writeUTF("PORT " + hostName + " " + data_port);
			// System.out.println("PORT command completed\n");

			System.out.println("Enter the command: ");
			String command = scanner.nextLine();

			if (command.contains("STOR")) {
				dataoutputstream.writeUTF("STOR");
				sendFile(host, data_port, command);
			} else if (command.contains("RETR")) {
				dataoutputstream.writeUTF("RETR");
				receiveFile(host, data_port, command);
			}

			dataoutputstream.writeUTF("QUIT");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void sendFile(InetAddress host, int data_port, String command) {
		// create a socket
		// send the file

		System.out.println("In client upload file");

		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");
			System.out.println("client : In send file");

			DataOutputStream dout = new DataOutputStream(dataSocket.getOutputStream());
			String filename = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/client/sample.txt";
			File f = new File(filename);

			System.out.println("Sending File ...");

			FileInputStream fin = new FileInputStream(f);
			int ch;
			do {
				ch = fin.read();
				dout.writeUTF(String.valueOf(ch));
			} while (ch != -1);
			fin.close();
			System.out.println("File sending done");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void receiveFile(InetAddress host, int data_port, String command) {
		// create a socket
		// Receive the file
		System.out.println("In client download file");

		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");

			String fileName = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/client/sample_inclient.txt";

			File f = new File(fileName);
			DataInputStream din = new DataInputStream(dataSocket.getInputStream());
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
			System.out.println("Client: File Received successfully");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
