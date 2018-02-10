package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class Client {
	public static void main(String argv[]) throws Exception {
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
		while(clientSocket.isConnected()){
			outToServer.writeByte(1);
			outToServer.flush();
			long key = inFromServer.readLong();
			if(key % 10000 == 0)
				System.out.println("key: " + key);
		}
		clientSocket.close();
	}
}