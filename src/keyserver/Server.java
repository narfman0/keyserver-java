package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import keyserver.TCPClientHandler;
import keyserver.Settings.ServerType;

public class Server {
	private static AtomicLong key = new AtomicLong();
	
	public static void main(String args[] ) throws Exception {
		if(Settings.TYPE == ServerType.UDP)
			runUDP();
		else
			runTCP();
	}
	
	public static void runUDP() throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(16);
		DatagramSocket socket = new DatagramSocket(Settings.PORT);
		byte[] buffer = new byte[1];
		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			executor.execute(new UDPClientHandler(socket, packet, key));
		}
	}
	
	public static void runTCP() throws Exception{
		ServerSocket socket = new ServerSocket(Settings.PORT);
		while (true) {
			Socket s = socket.accept();
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			Thread t = new TCPClientHandler(s, is, os, key);
			t.start();
		}
	}
}