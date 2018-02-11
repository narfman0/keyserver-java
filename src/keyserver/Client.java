package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

class Client {
	public static void main(String argv[]) throws Exception {
		switch(Settings.TYPE){
		case UDP_MULTI:
			runUDPMulti();
			break;
		case UDP:
			runUDP();
			break;
		case TCP:
			runTCP();
			break;
		}
	}
	
	public static void runUDP(int hostPort) throws Exception{
		System.out.println("UDP client using server port: " + hostPort);
		InetAddress address = InetAddress.getByName("localhost");
		DatagramSocket socket = new DatagramSocket();
		byte[] buffer = new byte[1],
				receiptBuffer = new byte[8];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, hostPort),
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
	
	public static void runUDPMulti() throws Exception{
		Random rand = new Random();
		runUDP(Settings.PORT + rand.nextInt(Settings.SERVER_PORTS));
	}
	
	public static void runUDP() throws Exception{
		runUDP(Settings.PORT);
	}
	
	public static void runTCP() throws Exception{
		System.out.println("TCP client using server port: " + Settings.PORT);
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