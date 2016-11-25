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

			System.out.println("Enter the command: ");
			String command = scanner.nextLine();
			
			if(command.contains("STOR")){
				uploadFile(host, data_port, command);
			}else if(command.contains("RETR")){
				downloadFile(host, data_port, command);
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

	private static void downloadFile(InetAddress host, int data_port, String command) {
		//create a socket
		//Receive the file
		System.out.println("In client download file");
		
		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void uploadFile(InetAddress host, int data_port, String command) {
		//create a socket
		//send the file
		
		System.out.println("In client upload file");
		
		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Control Connection");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*private static void downloadFile() {
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

	}*/
}
