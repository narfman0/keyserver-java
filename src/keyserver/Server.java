package keyserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import keyserver.TCPClientHandler;

public class Server {
	private static AtomicLong key = new AtomicLong();
	
	public static void main(String args[] ) throws Exception {
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
	
	public static void runUDPMulti() throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(Settings.SERVER_PORTS);
		for(int i=0; i<Settings.SERVER_PORTS; i++){
			int port = Settings.PORT + i;
			System.out.println("UDP server binding port: " + port);
			executor.execute(new UDPServer(port));
		}
		 while (!executor.isTerminated()){}
	}
	
	public static void runUDP() throws Exception{
		ExecutorService executor = Executors.newFixedThreadPool(16);
		DatagramSocket socket = new DatagramSocket(Settings.PORT);
		while (true) {
			byte[] buffer = new byte[1];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			executor.execute(new UDPClientHandler(socket, packet, key));
		}
	}
	
	public static void runTCP() throws Exception{
		ServerSocket socket = new ServerSocket(Settings.PORT);
		while (true) {
			Socket s = socket.accept();
			Thread t = new TCPClientHandler(s, key);
			t.start();
		}
	}
}