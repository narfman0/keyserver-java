package keyserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class ClientHandler extends Thread{
	private static final ExponentialMovingAverage ema = new ExponentialMovingAverage(.05);
	private static long lastTime = 0;
	private final DataInputStream is;
	private final DataOutputStream os;
	private final AtomicLong key;

	public ClientHandler(Socket s, DataInputStream is, DataOutputStream os, AtomicLong key) {
		this.is = is;
		this.os = os;
		this.key = key;
	}

	@Override
	public void run() {
		while(true){
			try {
				is.readByte();
				long keyValue = key.getAndIncrement();
				os.writeLong(keyValue);
				os.flush();
				if(keyValue % 100000 == 0){
					long newTime = System.currentTimeMillis();
					double diffSeconds = (newTime - lastTime) / 1000.0;
					lastTime = newTime;
					int emaValue = (int)ema.average(100000.0 / diffSeconds);
					System.out.println("Created key: " + keyValue + " keys/second: " + emaValue);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
