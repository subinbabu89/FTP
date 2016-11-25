package ftp.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class DataConnection implements Runnable {

	private int server_data_port;
	private String command;
	private Socket req_socket;

	public DataConnection(Socket req_socket, String command) {
		this.req_socket = req_socket;
		this.command = command;
	}

	@Override
	public void run() {

		System.out.println("In run method");
		if (command.equals("STOR"))
			System.out.println("In server save file");
			//uploadFile("sample.txt");
		else if (command.equals("RETR"))
			System.out.println("In server send file");
			//downloadFile("sampleFile.txt");
	}

	private void uploadFile(String fileName) {
		String fileContent = "This is sample content to be written to the file";
		File file = new File("D:/SAHANA/" + fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();

				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(fileContent);
				bufferedWriter.close();

				System.out.println("Finished writing to file");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void downloadFile(String string) {

		FileReader fr;
		try {
			fr = new FileReader("D:/SAHANA/sample.txt");

			int i;
			while ((i = fr.read()) != -1)
				System.out.print((char) i);

			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

}
