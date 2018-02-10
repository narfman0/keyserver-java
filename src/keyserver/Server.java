package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import keyserver.ClientHandler;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String args[] ) throws Exception {
		ServerSocket socket = new ServerSocket(6789);
		AtomicLong key = new AtomicLong();

		while (true) {
			Socket s = socket.accept();
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			Thread t = new ClientHandler(s, is, os, key);
			t.start();
		}
	}
}