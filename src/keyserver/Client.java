package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import keyserver.Settings.ServerType;

class Client {
	public static void main(String argv[]) throws Exception {
		if(Settings.TYPE == ServerType.UDP)
			runUDP();
		else
			runTCP();
	}
	
	public static void runUDP() throws Exception{
		InetAddress address = InetAddress.getByName("localhost");
		DatagramSocket socket = new DatagramSocket();
		byte[] buffer = new byte[1],
				receiptBuffer = new byte[8];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Settings.PORT),
				receipt;
		while(true){
			socket.send(packet);
			receipt = new DatagramPacket(receiptBuffer, receiptBuffer.length);
			socket.receive(receipt);
			long key = ByteBuffer.wrap(receiptBuffer).getLong();
			if(key % 10000 == 0)
				System.out.println("key: " + key);
		}
	}
	
	public static void runTCP() throws Exception{
		Socket clientSocket = new Socket("localhost", Settings.PORT);
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