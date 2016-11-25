package ftp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private static Socket clientSocket;

	public static void main(String[] args) {

		DataOutputStream dataoutputstream = null;
		DataInputStream datainputstream = null;

		try {
			InetAddress host = InetAddress.getByName("localhost");

			clientSocket = new Socket(host, 21);
			System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

			datainputstream = new DataInputStream(clientSocket.getInputStream());
			dataoutputstream = new DataOutputStream(clientSocket.getOutputStream());

			dataoutputstream.writeUTF("USER sahana");

			dataoutputstream.writeUTF("PASV");

			int data_port = Integer.parseInt(datainputstream.readUTF());

			System.out.println("The data port received at the client side is " + data_port);

			// dataoutputstream.writeUTF("PORT " + host + " " + data_port);

			// String fileName = "D:/SAHANA/sample.txt";
			// dataoutputstream.writeUTF("STOR " + fileName);

			// downloadFile();

			// File file = new File(fileName);
			dataoutputstream.writeUTF("quit");

			/*
			 * dataoutputstream.writeUTF("hello from client");
			 * System.out.println("read in client" + datainputstream.readUTF());
			 */
			// Make the client connect to that data port
			// Socket client_data_Socket = new Socket(host, 1029); //For data
			// connection

			System.out.println("Data connection done from client side");
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

	private static void downloadFile() {
		File file = new File("D:/SAHANA/sam.txt");
		long length = file.length();
		byte[] bytes = new byte[16 * 1024];

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			InputStream in = new FileInputStream(file);
			OutputStream out;

			out = clientSocket.getOutputStream();

			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
