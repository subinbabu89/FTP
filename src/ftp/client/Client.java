package ftp.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

			while(true){
				System.out.println("[ MENU ]");
				System.out.println("1. Upload File");
				System.out.println("2. Download File");
				System.out.println("3. Quit");
				System.out.print("\nEnter Choice :");
				int choice;
				
				choice = Integer.parseInt(scanner.nextLine());
				
				if(choice == 3){
					System.out.println("Disconnected...");
					dataoutputstream.writeUTF("QUIT");
					disconnectClient();
				}
				
				dataoutputstream.writeUTF("USER " + username);

				dataoutputstream.writeUTF("PASV");

				int data_port = Integer.parseInt(datainputstream.readUTF());

				if(choice == 1){
					System.out.println("Enter the filepath: ");
					String filePath = scanner.nextLine();
					
					dataoutputstream.writeUTF("STOR");
					
					sendFile(host, data_port, filePath);
					
				}else if(choice == 2){
					System.out.println("Enter the filename: ");
					String fileName = scanner.nextLine();
					
					dataoutputstream.writeUTF("RETR");
					
					receiveFile(host, data_port, fileName);
				}
			}
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

	private static String getFileName(String filePath) {
		filePath = filePath.replace("\\", "/");
		filePath = filePath.replaceAll("/", "\\\\");
		Path p = Paths.get(filePath);
		String fileName = p.getFileName().toString();
		return fileName;
	}

	private static void disconnectClient() {
		System.exit(1);
	}

	private static void sendFile(InetAddress host, int data_port, String filePath) {
		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");

			DataOutputStream dout = new DataOutputStream(dataSocket.getOutputStream());
			filePath = filePath.replace("\\", "/");
			
			File f = new File(filePath);

			String fileName = getFileName(filePath);
			dout.writeUTF(fileName);
			
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

	private static void receiveFile(InetAddress host, int data_port, String fileName) {

		try {
			Socket dataSocket = new Socket(host, data_port);
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");

			String filePath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/" + fileName;
			//String fileName = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/client/sample_inclient.txt";

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
