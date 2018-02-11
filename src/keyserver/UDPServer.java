package keyserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicLong;

public class UDPServer implements Runnable{
	private static AtomicLong key = new AtomicLong();
	private final DatagramSocket socket;

	public UDPServer(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}

	@Override
	public void run() {
		while(true){
			byte[] buffer = new byte[1];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			new UDPClientHandler(socket, packet, key).run();
		}
	}
}