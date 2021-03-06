package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class TCPClientHandler extends Thread{
	private static final TimeMonitor monitor = new TimeMonitor();
	private final DataInputStream is;
	private final DataOutputStream os;
	private final AtomicLong key;

	public TCPClientHandler(Socket s, AtomicLong key) throws IOException {
		this.key = key;
		is = new DataInputStream(s.getInputStream());
		os = new DataOutputStream(s.getOutputStream());
	}

	@Override
	public void run() {
		while(true){
			try {
				is.readByte();
				long keyValue = key.getAndIncrement();
				os.writeLong(keyValue);
				os.flush();
				monitor.update(keyValue);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
