package ftp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

public class ControlConnection implements Runnable {

	Socket request_socket;
	Socket telnet_client_socket;
	
	private DataOutputStream dataoutputstream = null;
	private DataInputStream datainputstream = null;
	
	private DataOutputStream telnet_dout = null;
	private DataInputStream telnet_din = null;

	public ControlConnection(Socket request_socket, Socket telnet_client_socket) throws IOException {
		this.request_socket = request_socket;
		this.telnet_client_socket = telnet_client_socket;
	}

	@Override
	public void run() {
		System.out.println("Client : In run ");
		int data_port = 0;
		Socket requ_socket = null;
		String username = null;

		try {
			System.out.println("In while");

			telnet_din = new DataInputStream(telnet_client_socket.getInputStream());
			telnet_dout = new DataOutputStream(telnet_client_socket.getOutputStream());

			String telnet_user_string = telnet_din.readUTF();
			System.out.println(" Server: The user string received is " + telnet_user_string);
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/ec2-user/Credentials.txt"));
			String LoginInfo=new String("");
			boolean allow=false;
			
			StringTokenizer tokens=new StringTokenizer(telnet_user_string, "_");
			tokens.nextToken();
			String usertoken = tokens.nextToken();
			String password = tokens.nextToken();
			System.out.println("The username and password is " + usertoken + " " + password);
			
			/* Add code to add the usernames to the file*/
			while((LoginInfo=bufferedReader.readLine())!=null)
	        {
	            StringTokenizer st=new StringTokenizer(LoginInfo, " ");
	            if(usertoken.equals(st.nextToken()) && password.equals(st.nextToken()))
	            {
	            	allow=true;
	            	telnet_dout.writeUTF("true");
	                break;
	            }else{
	            	telnet_dout.writeUTF("false");
	            }
	        }
			//System.out.println("The flag is " + allow);
			
			while (true) {
				
				datainputstream = new DataInputStream(request_socket.getInputStream());
				dataoutputstream = new DataOutputStream(request_socket.getOutputStream());

				System.out.println("dios of req sock created");
				
				String command = datainputstream.readUTF();
				String cmd = null;

				System.out.println("the cmd read is " + command);

				if (command.equals("PASV"))
					cmd = "PASV";
				else if (command.equals("MODE")){
					cmd = "MODE";
				}else if (command.equals("TYPE")){
					cmd = "TYPE";
				}
				/*
				 * else if(command.contains("PORT")){ cmd = "PORT"; }
				 */else if (command.contains("STOR")) {
					System.out.println("stor received");
					cmd = "STOR";
				} else if (command.contains("RETR")) {
					System.out.println("retr received");
					cmd = "RETR";
				} else if (command.contains("USER")) {
					System.out.println("USER cmd received");
					cmd = "USER";
				} else if (command.equals("QUIT"))
					cmd = "QUIT";

				switch (cmd) {

					case "MODE":
						System.out.println("in MODe");
						dataoutputstream.writeUTF("200 OK Message : Mode is Stream");
						break;
						
					case "TYPE":
						System.out.println("in TYPE");
						dataoutputstream.writeUTF("200 OK Message : Type is ASCII");
						break;
						
					case "PASV":
						data_port = generateDataPort();
						System.out.println("The data port is " + data_port);
						dataoutputstream.writeUTF(String.valueOf(data_port));
						ServerSocket serverSocket = new ServerSocket(data_port);
						System.out.println("Server listening on port " + data_port);
						requ_socket = serverSocket.accept();
						break;
	
					/*
					 * case "PORT": ServerSocket serverSocket = new
					 * ServerSocket(data_port); System.out.println(
					 * "Server listening on port " + data_port); requ_socket =
					 * serverSocket.accept(); break;
					 */
	
					case "USER":
						username = checkUser(command);
						listFiles(username);
						break;
	
					case "STOR": {
						System.out.println("In store");
						Runnable dataConnection = new DataConnection(username, command, requ_socket);
						new Thread(dataConnection).start();
						break;
					}
	
					case "RETR": {
						System.out.println("In retrieve");
						Runnable dataConnection = new DataConnection(username, command, requ_socket);
						new Thread(dataConnection).start();
						break;
					}
	
					case "QUIT": {
						System.out.println("quit command received");
						return;
					}
					
					default: {
						System.out.println("In default");
						break;
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
		String userPath = "/home/ec2-user/" + username;
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

		if (listOfFiles.length > 0)
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
