package keyserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

public class UDPClientHandler implements Runnable{
	private static final TimeMonitor monitor = new TimeMonitor();
	private final DatagramSocket socket;
	private final DatagramPacket packet;
	private final AtomicLong key;

	public UDPClientHandler(DatagramSocket socket, DatagramPacket packet, AtomicLong key) {
		this.socket = socket;
		this.packet = packet;
		this.key = key;
	}

	@Override
	public void run() {
		byte[] sendBuffer = new byte[8];
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		long keyValue = key.getAndIncrement();
		ByteBuffer.wrap(sendBuffer).putLong(keyValue);
		send(new DatagramPacket(sendBuffer, sendBuffer.length, address, port));
		monitor.update(keyValue);
	}
	
	private void send(DatagramPacket packet){
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
