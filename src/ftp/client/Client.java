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
import java.util.StringTokenizer;

public class Client {

	private static Socket clientSocket;
	
	private static Socket telnetClientSocket;

	public static void main(String[] args) {
		DataOutputStream dataoutputstream = null;
		DataInputStream datainputstream = null;
		
		DataOutputStream telnet_dout = null;
		DataInputStream telnet_din = null;

		try {
			
			Scanner scanner = new Scanner(System.in);

			System.out.println("Enter the host to connect: ");
			String hostName = scanner.nextLine();

			System.out.println("Enter the username: ");
			String username = scanner.nextLine();
			
			InetAddress host = InetAddress.getByName(hostName);
			//String host = "ec2-54-214-92-142.us-west-2.compute.amazonaws.com";
			//String host = "54.214.92.142";
			clientSocket = new Socket(host, 21);

			System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + " for Control Connection");
			
			telnetClientSocket = new Socket(host, 23);
			
			System.out.println("Connected to " + telnetClientSocket.getRemoteSocketAddress() + " for telnet Connection");

			telnet_din = new DataInputStream(telnetClientSocket.getInputStream());
			telnet_dout = new DataOutputStream(telnetClientSocket.getOutputStream());
			
			String success = "false";
			String password = null;
			
			while(success.equals("false")){
				System.out.println("Enter the password: ");
				password = scanner.nextLine();
				
				String telnet_user_string = "telnetd_" + username + "_" + password;
				System.out.println("Client: The user string is " + telnet_user_string);
				telnet_dout.writeUTF(telnet_user_string);
				
				success = telnet_din.readUTF();
			}
			
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
				
				dataoutputstream.writeUTF("MODE");
				System.out.println("The MODE message from server is " + datainputstream.readUTF());
				
				dataoutputstream.writeUTF("TYPE");
				System.out.println("The TYPE message from server is " + datainputstream.readUTF());

				dataoutputstream.writeUTF("USER " + username);

				createUserDirectory(username);
				
				dataoutputstream.writeUTF("PASV");

				int data_port = Integer.parseInt(datainputstream.readUTF());

				System.out.println("data port is " + data_port );
				System.out.println("The choice made is " + choice);
				
				if(choice == 1){
					System.out.println("In choice 1");
					System.out.println("Enter the filepath: ");
					String filePath = scanner.nextLine();
					
					dataoutputstream.writeUTF("STOR");
					
					sendFile(host, data_port, filePath);
					
				}else if(choice == 2){
					System.out.println("Enter the filename: ");
					String fileName = scanner.nextLine();
					
					dataoutputstream.writeUTF("RETR");
					
					receiveFile(host, data_port, fileName, username);
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

	private static void receiveFile(InetAddress host, int data_port, String fileName, String username) {

		try {
			Socket dataSocket = new Socket(host, data_port);
			
			DataInputStream din = new DataInputStream(dataSocket.getInputStream());
			DataOutputStream dout = new DataOutputStream(dataSocket.getOutputStream());
			
			System.out.println("Connected to " + dataSocket.getRemoteSocketAddress() + " for Data Connection");

			//String filePath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/" + username + "/" + fileName;

			String filePath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/" + username + "/" + fileName;
			
			dout.writeUTF(fileName);
			
			File f = new File(filePath);
			
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
	
	private static String createUserDirectory(String username) {
		String userPath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/" + username;
		File file = new File(userPath);
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
}
