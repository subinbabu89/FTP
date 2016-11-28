package ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataConnection implements Runnable {

	private int server_data_port;
	private String command;
	private Socket req_socket;
	private String username;
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	
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

	private void receiveFile() {
		
		DataInputStream din;

		try {
			din = new DataInputStream(req_socket.getInputStream());
			
			String fname = din.readUTF();
			
			//String filePath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/src/ftp/server/sampleServer.txt";
			String filePath = "/home/ec2-user/" + username + "/" + fname;
			
			File f = new File(filePath);

			FileOutputStream fout = new FileOutputStream(f);
			
			//lock.writeLock().lock();
			
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
			
			//lock.writeLock().unlock();
			
			System.out.println("server: file received successfully");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendFile() {
		
		DataOutputStream dout;

		DataInputStream din;
		
		try {
			dout = new DataOutputStream(req_socket.getOutputStream());
			din = new DataInputStream(req_socket.getInputStream());
			String filename = din.readUTF(); 
			System.out.println("The file required is " + filename);
			
			//String filePath = "D:/SAHANA/Advanced SE/FTP/CSE6324_FTP/" + username + "/" + filename;
			
			String filePath = "/home/ec2-user/" + username + "/" + filename;
					
			File f = new File(filePath);
			FileInputStream fin = new FileInputStream(f);
			
			//lock.readLock().lock();
			
			int ch;
			do {
				ch = fin.read();
				dout.writeUTF(String.valueOf(ch));
			} while (ch != -1);
			fin.close();
			
			//lock.readLock().lock();
			
			System.out.println("Server: File Sending done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
